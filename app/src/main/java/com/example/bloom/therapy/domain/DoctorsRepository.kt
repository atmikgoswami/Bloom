package com.example.bloom.therapy.domain

import com.example.bloom.utils.Result
import com.example.bloom.therapy.data.models.Doctors

interface DoctorsRepository {
    suspend fun getDoctors(): Result<List<Doctors>>
}