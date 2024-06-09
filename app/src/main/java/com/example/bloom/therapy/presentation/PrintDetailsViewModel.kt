package com.example.bloom.therapy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.therapy.data.models.PrintDetails
import com.example.bloom.therapy.domain.GeneratePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PrintDetailsViewModel @Inject constructor(private val generatePdfUseCase: GeneratePdfUseCase) : ViewModel() {

    fun generatePdf(printDetails: PrintDetails) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){generatePdfUseCase(printDetails)}
        }
    }
}