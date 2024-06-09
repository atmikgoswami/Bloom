package com.example.bloom.chatbot.domain

import android.graphics.Bitmap
import com.example.bloom.chatbot.data.models.Chat

interface ChatRepository {
    suspend fun getResponse(prompt: String): Chat
    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat
}