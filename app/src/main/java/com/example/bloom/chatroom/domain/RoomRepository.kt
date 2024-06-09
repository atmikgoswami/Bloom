package com.example.bloom.chatroom.domain

import com.example.bloom.utils.Result
import com.example.bloom.chatroom.data.models.ChatRoom

interface RoomRepository{
    suspend fun createRoom(name:String): Result<Unit>
    suspend fun getRooms(): Result<List<ChatRoom>>
}