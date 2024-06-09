package com.example.bloom.therapy.data.repository

import com.example.bloom.therapy.data.models.Patient
import com.example.bloom.utils.Result
import com.example.bloom.therapy.domain.PatientRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(): PatientRepository{
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun bookAppointment(doctorId: String, patient: Patient): Result<Unit> = try {
        db.collection("doctors").document(doctorId)
            .collection("appointments").add(patient).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun getAppointments(doctorId: String): Flow<List<Patient>> = callbackFlow {
        val subscription = db.collection("doctors").document(doctorId)
            .collection("appointments")
            .orderBy("appointmentDate")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    trySend(it.documents.map { doc ->
                        doc.toObject(Patient::class.java)!!.copy()
                    }).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }

    override suspend fun getBookedTimeSlots(doctorId: String, date: String): List<String> = try {
        val appointments = db.collection("doctors").document(doctorId)
            .collection("appointments")
            .whereEqualTo("appointmentDate", date)
            .get().await()
        appointments.documents.mapNotNull { it.getString("appointmentTime") }
    } catch (e: Exception) {
        emptyList()
    }
}