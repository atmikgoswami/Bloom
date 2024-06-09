package com.example.bloom.chatroom.data.repository

import com.example.bloom.utils.Result
import com.example.bloom.chatroom.data.models.ChatRoom
import com.example.bloom.chatroom.domain.RoomRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor():RoomRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override suspend fun createRoom(name: String): Result<Unit> = try {
        val room = ChatRoom(name = name)
        db.collection("rooms").add(room).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun getRooms(): Result<List<ChatRoom>> = try {
        val querySnapshot = db.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(ChatRoom::class.java)!!.copy(id = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Result.Error(e)
    }
}