package com.example.bloom.therapy.domain

import com.example.bloom.therapy.data.models.PrintDetails

interface GeneratePdfUseCase {
    suspend operator fun invoke(printDetails: PrintDetails)
}