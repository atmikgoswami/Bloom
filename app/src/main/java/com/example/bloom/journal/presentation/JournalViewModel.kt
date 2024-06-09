package com.example.bloom.journal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.journal.data.models.Journal
import com.example.bloom.utils.Result
import dagger.Lazy
import com.example.bloom.journal.domain.repository.JournalRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: Lazy<JournalRepository>,
    ) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        repository.get()
    }

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> get() = _journals


    fun addJournal(title: String, text: String, journalId: String = "") {
        auth.currentUser?.uid?.let { userId ->
            val journal = Journal(
                id = journalId,
                title = title,
                text = text
            )
            viewModelScope.launch {
                when (withContext(Dispatchers.IO) {repository.get().addJournal(journal, userId)}) {
                    is Result.Success<*> -> {
                        loadJournals()
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }

    fun deleteJournal(journalId: String){
        auth.currentUser?.uid?.let{userId->
            viewModelScope.launch {
                when (repository.get().deleteJournal(journalId, userId)) {
                    is Result.Success<*> -> {
                        loadJournals()
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }

    fun updateJournal(journalId: String, updatedTitle: String, updatedText: String){
        auth.currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                when (repository.get().updateJournal(journalId, Journal(title = updatedTitle, text = updatedText), userId)) {
                    is Result.Success -> {
                        _journals.value = _journals.value.filterNot { it.id == journalId }
                        loadJournals()
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }

    fun loadJournals() {
        auth.currentUser?.uid?.let{userId->
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    repository.get().getUserJournals(userId)
                        .collect { _journals.value = it }
                }
            }
        }
    }
}
