package com.example.bloom.therapy.data.models

import com.google.gson.Gson

data class Doctors(
    val id: String = "",
    val name : String = "",
    val image: String = "",
    val qualification:String="",
    val sittingHours:String="",
    val address : String = "",
    val mobile : String = ""
){
    // Convert Doctor object to JSON string
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        // Convert JSON string to Doctor object
        fun fromJson(json: String): Doctors {
            return Gson().fromJson(json, Doctors::class.java)
        }
    }
}
