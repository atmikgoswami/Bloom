package com.example.bloom.therapy.domain

import com.example.bloom.utils.Result
import com.example.bloom.therapy.data.models.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    suspend fun bookAppointment(doctorId: String, patient: Patient): Result<Unit>
    suspend fun getAppointments(doctorId: String): Flow<List<Patient>>
    suspend fun getBookedTimeSlots(doctorId: String, date: String): List<String>
}