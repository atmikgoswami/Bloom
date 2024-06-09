package com.example.bloom.therapy.presentation

import androidx.lifecycle.ViewModel
import com.example.bloom.utils.Result
import androidx.lifecycle.viewModelScope
import com.example.bloom.therapy.data.models.Patient
import com.example.bloom.therapy.domain.PatientRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val repository: Lazy<PatientRepository>,
): ViewModel() {

    private val _appointments = MutableStateFlow<List<Patient>>(emptyList())
    val appointments: StateFlow<List<Patient>> get() = _appointments

    private val _bookedTimeSlots = MutableStateFlow<List<String>>(emptyList())
    val bookedTimeSlots: StateFlow<List<String>> = _bookedTimeSlots

    private val _doctorId = MutableStateFlow<String>("")

    init{
        repository.get()
    }

    fun setDoctorId(doctorId: String) {
        _doctorId.value = doctorId
        loadAppointments()
    }

    fun bookAppointment(name: String, age:String, symptoms:String, phoneNo:String, date:String, time:String, ) {
        val patient = Patient(
            patientName = name,
            patientMobileNo = phoneNo,
            appointmentDate = date,
            appointmentTime = time,
            patientAge = age,
            patientSymptoms = symptoms
        )
        viewModelScope.launch {
            when (withContext(Dispatchers.IO){repository.get().bookAppointment(_doctorId.value.toString(), patient)}) {
                is Result.Success -> Unit
                is Result.Error -> {

                }
            }
        }
    }

    fun fetchBookedTimeSlots(doctorId: String, date: String) {
        viewModelScope.launch {
            try {
                val bookedSlots = withContext(Dispatchers.IO){repository.get().getBookedTimeSlots(doctorId, date)}
                _bookedTimeSlots.value = bookedSlots
            } catch (e: Exception) {
                // Handle error
                _bookedTimeSlots.value = emptyList()
            }
        }
    }

    fun loadAppointments() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){repository.get().getAppointments(_doctorId.value.toString())
                .collect { _appointments.value = it }}
        }
    }
}