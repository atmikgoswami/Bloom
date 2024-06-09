package com.example.bloom.journal.data.models

import com.example.bloom.therapy.data.models.Doctors
import com.google.gson.Gson

data class Journal(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
{
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): Journal {
            return Gson().fromJson(json, Journal::class.java)
        }
    }
}