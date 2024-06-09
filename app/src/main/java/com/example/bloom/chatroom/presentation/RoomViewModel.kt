package com.example.bloom.chatroom.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.utils.Result
import com.example.bloom.chatroom.data.models.ChatRoom
import com.example.bloom.chatroom.domain.RoomRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: Lazy<RoomRepository>
): ViewModel(){

    init {
        repository.get()
    }

    private val _rooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val rooms: StateFlow<List<ChatRoom>> = _rooms

    fun createRoom(name: String) {
        viewModelScope.launch {
            when (withContext(Dispatchers.IO){repository.get().createRoom(name)}) {
                is Result.Success<*> -> {
                    loadRooms()
                }
                is Result.Error -> {
                    // Handle error case
                }
            }
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO){repository.get().getRooms()}) {
                is Result.Success<List<ChatRoom>> -> _rooms.value = result.data
                is Result.Error -> {
                }
            }
        }
    }
}