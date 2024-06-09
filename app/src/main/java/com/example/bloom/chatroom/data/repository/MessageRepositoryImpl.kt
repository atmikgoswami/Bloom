package com.example.bloom.chatroom.data.repository

import android.util.Log
import com.example.bloom.utils.Result
import com.example.bloom.chatroom.data.models.Message
import com.example.bloom.chatroom.domain.MessageRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor():MessageRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override suspend fun sendMessage(roomId: String, message: Message): Result<Unit> = try {
        db.collection("rooms").document(roomId)
            .collection("messages").add(message).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun getChatMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val subscription = db.collection("rooms").document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    trySend(it.documents.map { doc ->
                        doc.toObject(Message::class.java)!!.copy(id = doc.id)
                    }).isSuccess
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun likeMessage(roomId: String, messageId: String, userId: String): Result<Unit> = try {
        val messageRef = db.collection("rooms").document(roomId)
            .collection("messages").document(messageId)

        Log.d("Yellow","$messageRef")

        val messageSnapshot = messageRef.get().await()
        val currentLikeCount = messageSnapshot.toObject(Message::class.java)?.likesCount?:0
        val currentLikedList = messageSnapshot.toObject(Message::class.java)?.likedBy?: emptyList()
        val newLikesCount = currentLikeCount + 1
        val newLikedList = currentLikedList.toMutableList().apply{add(userId)}

        val newMessage =
            messageSnapshot.toObject(Message::class.java)?.copy(likesCount = newLikesCount, likedBy = newLikedList)

        if (newMessage != null) {
            messageRef.set(newMessage)
        }

        Result.Success(Unit)
    } catch (e: Exception) {
        Log.d("red","Error in messagerepo")
        Result.Error(e)
    }
}