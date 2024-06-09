package com.example.bloom.profile.data.repository

import android.util.Log
import com.example.bloom.utils.Result
import com.example.bloom.profile.data.models.Profile
import com.example.bloom.profile.domain.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getProfileDetails(userId: String): Flow<Profile> = callbackFlow{

        val subscription = db.collection("users").document(userId)
            .collection("profiles")
            .document("profileDetails")
            .addSnapshotListener { documentSnapshot, _ ->
                documentSnapshot?.let { doc ->
                    val profile = doc.toObject(Profile::class.java)
                    if (profile != null) {
                        trySend(profile).isSuccess
                    } else {
                        close(Exception("Profile not found"))
                    }
                }
            }
        awaitClose { subscription.remove() }
    }.catch { e ->
        throw Exception("Failed to fetch profile details: ${e.message}")
    }

    override suspend fun updateMobileNumber(userId: String, mobileNumber: String) : Result<Unit> = try{

        val profileDocRef = db.collection("users").document(userId)
            .collection("profiles").document("profileDetails")

        val profileSnapshot = profileDocRef.get().await()
        val newProfile = profileSnapshot.toObject(Profile::class.java)?.copy(mobileNumber = mobileNumber)?:""

        profileDocRef.set(newProfile)
        Result.Success(Unit)
    }
    catch (e: Exception) {
        Log.d("red","Error in profileRepo")
        Result.Error(e)
    }

    override suspend fun updateEmergencyContact(userId: String, contactName:String, contactNo: String) : Result<Unit> = try{

        val profileDocRef = db.collection("users").document(userId)
            .collection("profiles").document("profileDetails")

        val profileSnapshot = profileDocRef.get().await()
        val newProfile = profileSnapshot.toObject(Profile::class.java)?.copy(emergencyContactNumber = contactNo, emergencyContactName = contactName)?:""

        profileDocRef.set(newProfile)
        Result.Success(Unit)
    }
    catch (e: Exception) {
        Log.d("red","Error in profileRepo")
        Result.Error(e)
    }
}
