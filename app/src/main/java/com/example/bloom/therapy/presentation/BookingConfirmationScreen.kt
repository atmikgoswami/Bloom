package com.example.bloom.therapy.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bloom.R
import com.example.bloom.navigation.nav_routes
import com.example.bloom.therapy.data.models.PrintDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintDetailsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PrintDetailsViewModel = hiltViewModel(),
    printDetails: PrintDetails
) {

    var selectedItemIndex1 by rememberSaveable {
        mutableIntStateOf(3)
    }

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
                                text = "Appointment Booked",
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
                            modifier = modifier.size(38.dp).padding(top=8.dp)
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
    )
    {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize())
        {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Image(
                    painter = painterResource(R.drawable.tick),
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.size(70.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Booking Confirmed",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = modifier.height(12.dp))
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top=160.dp, start=16.dp, end=8.dp))
            {
                Text(
                    text = "Patient Name: ${printDetails.patientName}",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = "Patient Age: ${printDetails.patientAge}",
                    style = TextStyle(
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = "Symptoms: ${printDetails.patientSymptoms}",
                    style = TextStyle(
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = "Appointment Date: ${printDetails.appointmentDate}",
                    style = TextStyle(
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = "Time Slot: ${printDetails.selectedTimeSlot}",
                    style = TextStyle(
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = "Doctor Name: ${printDetails.doctorName}",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = "Doctor Address: ${printDetails.doctorAddress}",
                    style = TextStyle(
                        fontSize = 25.sp,
                    )
                )
                Spacer(modifier = modifier.height(20.dp))

                Button(
                    onClick = { viewModel.generatePdf(printDetails) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Generate PDF")
                }
            }
        }
    }
}