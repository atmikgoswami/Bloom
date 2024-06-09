package com.example.bloom.therapy.data.repository

import com.example.bloom.therapy.data.models.Doctors
import com.example.bloom.utils.Result
import com.example.bloom.therapy.domain.DoctorsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DoctorsRepositoryImpl @Inject constructor() :DoctorsRepository{
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getDoctors(): Result<List<Doctors>> = try {
        val querySnapshot = db.collection("doctors").get().await()
        val doctors = querySnapshot.documents.map { document ->
            document.toObject(Doctors::class.java)!!.copy(id = document.id)
        }
        Result.Success(doctors)
    } catch (e: Exception) {
        Result.Error(e)
    }
}