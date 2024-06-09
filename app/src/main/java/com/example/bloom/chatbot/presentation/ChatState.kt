package com.example.bloom.chatbot.presentation

import android.graphics.Bitmap
import com.example.bloom.chatbot.data.models.Chat

data class ChatState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)