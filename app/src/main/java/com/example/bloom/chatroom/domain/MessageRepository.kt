package com.example.bloom.chatroom.domain

import com.example.bloom.chatroom.data.models.Message
import com.example.bloom.utils.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(roomId: String, message: Message): Result<Unit>
    suspend fun getChatMessages(roomId: String): Flow<List<Message>>
    suspend fun likeMessage(roomId: String, messageId: String, userId: String): Result<Unit>
}