package com.example.bloom.therapy.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bloom.R
import com.example.bloom.therapy.data.models.PrintDetails
import com.example.bloom.navigation.nav_routes
import com.example.bloom.therapy.data.models.Doctors
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorAppointmentScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PatientViewModel = hiltViewModel(),
    doctor: Doctors,
    onBookingConfirmed: (PrintDetails)->Unit
) {
    var selectedItemIndex1 by rememberSaveable {
        mutableIntStateOf(3)
    }

    val context = LocalContext.current
    val appointments by viewModel.appointments.collectAsState()
    val bookedTimeSlots by viewModel.bookedTimeSlots.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val appointmentDates by remember {
        derivedStateOf {
            appointments.map { LocalDate.parse(it.appointmentDate, formatter) }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.setDoctorId(doctor.id)
    }

    var patientName by remember { mutableStateOf("") }
    var patientPhoneNo by remember { mutableStateOf("") }
    var patientAge by remember { mutableStateOf("") }
    var patientSymptoms by remember { mutableStateOf("") }
    var appDate by remember { mutableStateOf("") }
    var selectedTimeSlot by remember { mutableStateOf("") }
    val calendarState = rememberSheetState()
    val scrollState = rememberLazyListState()
    val expanded1 = remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset == 0 } }

    val timeSlots = listOf(
        "10:00 AM - 12:00 PM",
        "12:00 PM - 02:00 PM",
        "02:00 PM - 04:00 PM",
        "04:00 PM - 06:00 PM"
    )

    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val disabledDates = remember {
        val disabled = mutableListOf<LocalDate>()
        // Add all dates before tomorrow to the disabled dates list
        var date = LocalDate.now()
        while (date.isBefore(tomorrow)) {
            disabled.add(date)
            date = date.plusDays(1)
        }
        // Add booked dates to the disabled dates list
        disabled.addAll(
            appointmentDates.filter { date ->
                val formattedDate = date.format(formatter)
                val bookedSlots = appointments.filter { it.appointmentDate == formattedDate }
                bookedSlots.size == timeSlots.size
            }
        )
        disabled
    }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            disabledDates = disabledDates,
            minYear = LocalDate.now().year,
        ),
        selection = CalendarSelection.Date { date ->
            appDate = date.toString()
            viewModel.fetchBookedTimeSlots(doctor.id, appDate)
        },
    )



    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier
                    .height(60.dp),
                title = {
                    Box(modifier = modifier.fillMaxWidth())
                    {
                        Row(modifier=modifier.fillMaxSize().padding(top=8.dp))
                        {
                            Text(
                                text = "Book Appointment",
                                color = colorResource(id = R.color.black),
                                modifier = modifier
                                    .heightIn(max = 48.dp)
                                    .padding(top = 8.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                ),
                            )
                        }

                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.Black,
                            contentDescription = "back",
                            modifier = modifier.size(38.dp).padding(top=12.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.inverseOnSurface) {
                nav_routes.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex1 == index,
                        onClick = {
                            selectedItemIndex1 = index
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex1) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    DoctorDetails(
                        doctorName = doctor.name,
                        doctorQualification = doctor.qualification,
                        doctorAddress = doctor.address,
                        doctorImage = doctor.image,
                        doctorMobileNo = doctor.mobile,
                        doctorSittingHours = doctor.sittingHours,
                        expanded = expanded1.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = patientName,
                            onValueChange = { newName -> patientName = newName },
                            label = { Text(text = "Patient Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10))
                                .padding(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = patientAge,
                            onValueChange = { newAge -> patientAge = newAge },
                            label = { Text(text = "Enter age") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10))
                                .padding(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = patientPhoneNo,
                            onValueChange = { newPhoneNo -> patientPhoneNo = newPhoneNo },
                            label = { Text(text = "Mobile Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10))
                                .padding(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = patientSymptoms,
                            onValueChange = { newSymptoms -> patientSymptoms = newSymptoms },
                            label = { Text(text = "Patient Symptoms") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10))
                                .padding(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(8.dp)
                                .border(
                                    BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(10)
                                )
                                .clickable {
                                    calendarState.show()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (appDate.isNotEmpty())
                                Text(text = appDate, modifier = Modifier.padding(start = 15.dp))
                            else
                                Text(text = "Date", modifier = Modifier.padding(start = 15.dp), color = MaterialTheme.colorScheme.onSurface)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Time Slot Selection using Dropdown Menu
                        var expanded by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(8.dp)
                                .border(
                                    BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(10)
                                )
                                .clickable {
                                    expanded = true
                                },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = selectedTimeSlot.ifEmpty { "Select Time Slot" },
                                modifier = Modifier.padding(start = 15.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                timeSlots.forEach { slot ->
                                    DropdownMenuItem(onClick = {
                                        if(slot !in bookedTimeSlots){
                                            selectedTimeSlot = slot
                                            expanded = false
                                        }
                                    }, enabled = slot !in bookedTimeSlots
                                    ) {
                                        Text(text = slot,
                                        color = if (slot in bookedTimeSlots) Color.Gray else MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            if (patientName.isNotBlank() && patientPhoneNo.isNotBlank() && patientAge.isNotBlank() && patientSymptoms.isNotBlank() && appDate.isNotBlank() && selectedTimeSlot.isNotBlank()) {
                                Toast.makeText(
                                    context,
                                    "Appointment Booked. We will contact you soon",
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.bookAppointment(
                                    patientName,
                                    patientAge,
                                    patientSymptoms,
                                    patientPhoneNo,
                                    appDate,
                                    selectedTimeSlot
                                )
                                val printDetails = PrintDetails(
                                    patientName,
                                    patientAge,
                                    patientPhoneNo,
                                    patientSymptoms,
                                    appDate,
                                    selectedTimeSlot,
                                    doctor.name,
                                    doctor.address,
                                    "Appointment confirmed"
                                )
                                onBookingConfirmed(printDetails)
                            } else {
                                Toast.makeText(context, "Please fill all the details", Toast.LENGTH_LONG).show()
                            }

                        }) {
                            Text(text = "Book Appointment")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DoctorDetails(
    doctorName: String,
    doctorQualification: String,
    doctorAddress: String,
    doctorImage: String,
    doctorMobileNo: String,
    doctorSittingHours: String,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    if (expanded) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(doctorImage).crossfade(true).build() ,
                contentDescription = "Profile picture",
                modifier = modifier
                    .size(90.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = doctorName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = doctorQualification,
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        )
        {
            FlowRow {
                Text(
                    text = "Address: ",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = doctorAddress,
                    style = TextStyle(
                        fontSize = 18.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow {
                Text(
                    text = "Sitting Hours: ",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = doctorSittingHours,
                    style = TextStyle(
                        fontSize = 18.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow {
                Text(
                    text = "Contact Info: ",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = doctorMobileNo,
                    style = TextStyle(
                        fontSize = 18.sp,
                    )
                )
            }

        }
    } else {
        Column(modifier=Modifier.padding(top=16.dp, start=10.dp)) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(doctorImage).crossfade(true).build(),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = doctorName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Book your Appointment",
                modifier = Modifier.padding(start=80.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            )
        }

    }
}