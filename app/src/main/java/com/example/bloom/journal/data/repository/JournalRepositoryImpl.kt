package com.example.bloom.journal.data.repository

import com.example.bloom.utils.Result
import com.example.bloom.journal.data.models.Journal
import com.example.bloom.journal.domain.repository.JournalRepository
import com.example.bloom.profile.data.models.Profile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject



class JournalRepositoryImpl @Inject constructor() : JournalRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun addJournal(journal: Journal, userId: String): Result<Unit> = try {
        val documentReference = db.collection("users").document(userId)
            .collection("journals").add(journal).await()
        val journalId = documentReference.id
        val journalWithId = journal.copy(id = journalId)
        db.collection("users").document(userId)
            .collection("journals").document(journalId)
            .set(journalWithId).await()

        val profileDocRef = db.collection("users").document(userId)
            .collection("profiles").document("profileDetails")

        val profileSnapshot = profileDocRef.get().await()
        val currentCount = profileSnapshot.toObject(Profile::class.java)?.journalsCount ?: 0
        val newCount = currentCount + 1

        val newProfile = Profile(newCount)
        profileDocRef.set(newProfile).await()

        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun deleteJournal(journalId: String, userId: String): Result<Unit> = try {
        db.collection("users").document(userId)
            .collection("journals").document(journalId)
            .delete().await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun updateJournal(journalId: String, updatedJournal: Journal, userId: String): Result<Unit> = try {
        db.collection("users").document(userId)
            .collection("journals").document(journalId)
            .set(updatedJournal).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun getUserJournals(userId: String): Flow<List<Journal>> = callbackFlow {
        val subscription = db.collection("users").document(userId)
            .collection("journals")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    val journals = it.documents.map { doc ->
                        val journal = doc.toObject(Journal::class.java)!!
                        journal.copy(id = doc.id)
                    }
                    trySend(journals).isSuccess
                }
            }
        awaitClose { subscription.remove() }
    }
}