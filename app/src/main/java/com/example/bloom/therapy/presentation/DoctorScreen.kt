package com.example.bloom.therapy.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bloom.R
import com.example.bloom.navigation.nav_routes
import com.example.bloom.therapy.data.models.Doctors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = hiltViewModel(),
    onAppointmentClicked: (Doctors) -> Unit
){
    var selectedItemIndex1 by rememberSaveable {
        mutableIntStateOf(3)
    }

    val doctors by viewModel.doctors.collectAsState()
    LaunchedEffect(Unit){
        viewModel.loadDoctors()
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
                                text = "Therapy",
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
    )
    {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize())
        {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                items(doctors) { doctor ->
                    DoctorItem(doctor = doctor, onAppointmentClicked)
                }
            }
        }
    }
}

@Composable
fun DoctorItem(doctor: Doctors, onAppointmentClicked:(Doctors)->Unit) {
    val customGreyColor = Color(android.graphics.Color.parseColor("#ececec"))
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
            .clickable { onAppointmentClicked(doctor) },
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(doctor.image).crossfade(true).build() ,
            contentDescription = "Doctor Image",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(80.dp))
                .border(2.dp, Color.White)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Row {

                    Text(
                        text = doctor.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top=8.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    //Spacer(modifier = Modifier.width(2.dp))
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "address", tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = doctor.address,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding( end=8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Icon(imageVector = Icons.Filled.Phone, contentDescription = "phone", tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = doctor.mobile,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Normal,
                        //modifier = Modifier.padding(start = 8.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Book Appointment >",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                run { onAppointmentClicked(doctor) }
                            },
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }
}
