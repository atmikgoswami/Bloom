package com.example.bloom.chatroom.data.models

data class Message(
    val id: String="",
    val senderName: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val sentByCurrentUser: Boolean = false,
    var isRemoved: Boolean = false,
    var likesCount: Int = 0,
    val likedBy: MutableList<String> = mutableListOf()
)
