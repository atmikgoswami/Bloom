package com.example.bloom.auth.presentation

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.bloom.R
import com.example.bloom.auth.domain.models.SignInResult
import com.example.bloom.auth.domain.models.UserData
import com.example.bloom.profile.data.models.Profile
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleAuthUiClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {

            val user = auth.signInWithCredential(googleCredentials).await().user
            val profile = Profile(journalsCount = 0)
            val profileDocRef = user?.let {
                db.collection("users").document(it.uid)
                    .collection("profiles").document("profileDetails")
            }
            val profileSnapshot = profileDocRef?.get()?.await()

            if (profileSnapshot != null) {
                if (!profileSnapshot.exists()) {
                    db.collection("users").document(user.uid)
                        .collection("profiles").document("profileDetails")
                        .set(profile)
                        .await()
                }
            }

            SignInResult(
                data = user?.run {
                    email?.let {
                        UserData(
                            userId = uid,
                            username = displayName,
                            email = it,
                            profilePictureUrl = photoUrl?.toString()
                        )
                    }
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        email?.let {
            UserData(
                userId = uid,
                username = displayName,
                email = it,
                profilePictureUrl = photoUrl?.toString()
            )
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}