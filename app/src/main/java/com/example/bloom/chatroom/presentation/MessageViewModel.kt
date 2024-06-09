package com.example.bloom.chatroom.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.chatroom.data.models.Message
import com.example.bloom.chatroom.domain.MessageRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.bloom.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: Lazy<MessageRepository>
): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val _roomId = MutableStateFlow("")

    init{
        repository.get()
    }

    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }

    fun sendMessage(text: String, name:String) {

        auth.currentUser?.uid?.let{userId->
            val message = Message(
                senderName = name,
                senderId = userId,
                text = text
            )
            viewModelScope.launch {
                when (withContext(Dispatchers.IO){repository.get().sendMessage(_roomId.value, message)}) {
                    is Result.Success<*> -> Unit
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){repository.get().getChatMessages(_roomId.value)
                .collect { _messages.value = it }}
        }
    }

    fun likeMessage(messageId: String) {
        auth.currentUser?.uid?.let { userId ->
            Log.d("ViewModel",messageId)
            viewModelScope.launch {
                when (withContext(Dispatchers.IO){repository.get().likeMessage(_roomId.value, messageId, userId)}) {
                    is Result.Success<*> -> Unit
                    is Result.Error -> {
                        // Handle error case
                    }
                }
            }
        }
    }
}