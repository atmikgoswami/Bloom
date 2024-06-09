package com.example.bloom.chatbot.data.repository

import android.graphics.Bitmap
import com.example.bloom.BuildConfig
import com.example.bloom.chatbot.data.models.Chat
import com.example.bloom.chatbot.domain.ChatRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor():ChatRepository {
    private val apiKey = BuildConfig.GEMINI_API_KEY

    override suspend fun getResponse(prompt: String): Chat {
        val generativeModel = GenerativeModel(
            modelName = BuildConfig.GEMINI_MODEL_NAME_1, apiKey = apiKey
        )

        try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )

        } catch (e: Exception) {
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }

    }

    override suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat {
        val generativeModel = GenerativeModel(
            modelName = BuildConfig.GEMINI_MODEL_NAME_2, apiKey = apiKey
        )

        try {

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }

            return Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )

        } catch (e: Exception) {
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }

    }
}