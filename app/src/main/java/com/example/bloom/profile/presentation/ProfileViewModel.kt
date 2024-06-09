package com.example.bloom.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.profile.data.models.Profile
import com.example.bloom.profile.domain.ProfileRepository
import com.example.bloom.utils.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Lazy<ProfileRepository>
): ViewModel()
{
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _profileState: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val profileState: StateFlow<Profile?> = _profileState

    init {
        repository.get()
    }

    fun fetchProfile() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.get().getProfileDetails(userId).collect { profile ->
                _profileState.value = profile
            }
        }
    }

    fun addMobileNumber(mobileNumber:String){
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            when(withContext(Dispatchers.IO){repository.get().updateMobileNumber(userId,mobileNumber)}){
                is Result.Success<*> -> Unit
                is Result.Error -> {
                    // Handle error case
                }
            }
        }
    }

    fun addEmergencyContact(contactName:String, contactNo:String){
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            when(withContext(Dispatchers.IO){repository.get().updateEmergencyContact(userId, contactName, contactNo)}){
                is Result.Success<*> -> Unit
                is Result.Error -> {
                    // Handle error case
                }
            }
        }
    }
}