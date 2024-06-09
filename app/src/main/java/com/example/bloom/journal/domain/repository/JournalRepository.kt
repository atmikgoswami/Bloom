package com.example.bloom.journal.domain.repository

import com.example.bloom.utils.Result
import com.example.bloom.journal.data.models.Journal
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    suspend fun addJournal(journal: Journal, userId: String): Result<Unit>
    suspend fun deleteJournal(journalId: String, userId: String): Result<Unit>
    suspend fun updateJournal(journalId: String, updatedJournal: Journal, userId: String): Result<Unit>
    suspend fun getUserJournals(userId: String): Flow<List<Journal>>
}

