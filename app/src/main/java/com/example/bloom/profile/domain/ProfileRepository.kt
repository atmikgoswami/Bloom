package com.example.bloom.profile.domain

import com.example.bloom.profile.data.models.Profile
import com.example.bloom.utils.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfileDetails(userId: String): Flow<Profile>
    suspend fun updateMobileNumber(userId: String, mobileNumber: String) : Result<Unit>
    suspend fun updateEmergencyContact(userId: String, contactName:String, contactNo: String) : Result<Unit>
}