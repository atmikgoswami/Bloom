package com.example.bloom.therapy.data.models

import com.google.gson.Gson

data class PrintDetails(
    val patientName: String,
    val patientAge: String,
    val patientMobile: String,
    val patientSymptoms: String,
    val appointmentDate: String,
    val selectedTimeSlot: String,
    val doctorName: String,
    val doctorAddress: String,
    val bookingDetails: String,
){
    // Convert Doctor object to JSON string
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        // Convert JSON string to Doctor object
        fun fromJson(json: String): PrintDetails {
            return Gson().fromJson(json, PrintDetails::class.java)
        }
    }
}