package com.example.bloom.therapy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.therapy.data.models.Doctors
import com.example.bloom.utils.Result
import com.example.bloom.therapy.domain.DoctorsRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val repository: Lazy<DoctorsRepository>,
):ViewModel() {

    init{
        repository.get()
    }

    private val _doctors = MutableStateFlow<List<Doctors>>(emptyList())
    val doctors: StateFlow<List<Doctors>> = _doctors

    fun loadDoctors() {
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO){repository.get().getDoctors()}) {
                is Result.Success -> _doctors.value = result.data
                is Result.Error -> {

                }
            }
        }
    }
}