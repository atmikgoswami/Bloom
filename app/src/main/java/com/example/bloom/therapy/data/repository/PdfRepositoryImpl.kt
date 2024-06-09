package com.example.bloom.therapy.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.bloom.R
import com.example.bloom.therapy.data.models.PrintDetails
import com.example.bloom.therapy.domain.GeneratePdfUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GeneratePdfUseCaseImpl @Inject constructor(private val context: Context) :
    GeneratePdfUseCase {

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun invoke(printDetails: PrintDetails) {
        val pageWidth = 612
        val pageHeight = 792
        val pdfDocument = PdfDocument()

        // Create a page for the PDF
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Load logo from resources
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
        val scaledLogoBitmap = Bitmap.createScaledBitmap(logoBitmap, 100, 100, false)
        canvas.drawBitmap(scaledLogoBitmap, 20F, 20F, Paint())

        // Load tick mark from resources
        val tickMarkBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.tick)
        val scaledTickMarkBitmap = Bitmap.createScaledBitmap(tickMarkBitmap, 100, 100, false)
        canvas.drawBitmap(scaledTickMarkBitmap, 256F, 150F, Paint())

        // Write "Booking Confirmed!"
        val paint = Paint()
        paint.textSize = 18F
        paint.color = Color.BLACK
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("BOOKING CONFIRMED!", 220F, 300F, paint)
        paint.textSize = 25F
        canvas.drawText("BLOOM", 110F, 80F, paint)

        // Get the current date
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())

        paint.textSize = 12F
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Invoice No. 12345", pageWidth - 150F, 60F, paint)
        canvas.drawText(currentDate, pageWidth - 150F, 80F, paint)

        // Write patient details
        paint.textSize = 12F
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("DOCTOR NAME: ${printDetails.doctorName}", 40F, 340F, paint)
        canvas.drawText("PATIENT NAME: ${printDetails.patientName}", 40F, 440F, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("SESSION DATE: ${printDetails.appointmentDate}", 40F, 360F, paint)
        canvas.drawText("SESSION TIME: ${printDetails.selectedTimeSlot}", 40F, 380F, paint)

        canvas.drawText("AGE: ${printDetails.patientAge}", 40F, 460F, paint)
        canvas.drawText("MOBILE NO: ${printDetails.patientMobile}", pageWidth - 180F, 460F, paint)
        canvas.drawText("SYMPTOMS: ${printDetails.patientSymptoms}", 40F, 480F, paint)

        // Write clinic details
        canvas.drawText("BLOOM PVT. LTD.", 40F, pageHeight - 100F, paint)
        canvas.drawText("2/3, Mahatma Gandhi Road", 40F, pageHeight - 80F, paint)
        canvas.drawText("Block A, New Friends Colony", 40F, pageHeight - 60F, paint)
        canvas.drawText("New Delhi", 40F, pageHeight - 40F, paint)
        canvas.drawText("Delhi 110065", 40F, pageHeight - 20F, paint)

        // Finish the page
        pdfDocument.finishPage(page)

        // Save the PDF to external storage
        savePdfToDownloads(pdfDocument)

        // Close the document
        pdfDocument.close()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun savePdfToDownloads(pdfDocument: PdfDocument) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val uniqueFileName = generateUniqueFileName(downloadsDir, "appointment_details.pdf")
        // Create a new file with the unique name
        val file = File(downloadsDir, uniqueFileName)
        try {
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun generateUniqueFileName(directory: File, fileName: String): String {
    var uniqueFileName = fileName
    var counter = 1
    while (File(directory, uniqueFileName).exists()) {
        // Append a counter to the file name to make it unique
        uniqueFileName = "${fileName.substringBeforeLast(".pdf")}_$counter.pdf"
        counter++
    }
    return uniqueFileName
}
