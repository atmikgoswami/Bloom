package com.example.bloom.profile.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.bloom.R
import com.example.bloom.auth.domain.models.UserData

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userData: UserData?,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    navigateToAchievements:(Long) ->Unit
)
{
    viewModel.fetchProfile()
    val profile by viewModel.profileState.collectAsState()
    var showEditDialog1 by remember { mutableStateOf(false) }
    var showEditDialog2 by remember { mutableStateOf(false) }
    var newMobileNumber by remember { mutableStateOf("") }
    var newContactName by remember { mutableStateOf("") }
    var newContactNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(modifier = modifier
                .height(60.dp),
                title = {
                    Box(
                        modifier = modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "Bloom Profile",
                            color = colorResource(id = R.color.black),
                            modifier = modifier
                                .heightIn(max = 48.dp)
                                .padding(top = 8.dp, bottom = 8.dp)
                                .align(Alignment.CenterStart),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            ),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.Black,
                            contentDescription = "back",
                            modifier = modifier
                                .size(38.dp)
                                .padding(top = 12.dp)
                        )
                    }
                }
            )
        },
    )
    {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it))
        {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp)
            )
            {
                if(userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(top = 15.dp)
                            .border(4.dp, Color.Gray, RoundedCornerShape(100))
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if(userData?.username != null) {
                    Text(
                        text = userData.username,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if(userData?.username != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                            .clip(RoundedCornerShape(20))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .border(1.dp,Color.Gray,RoundedCornerShape(20))
                    )
                    {
                        Text(
                            text = "Email : ${userData.email}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(20))
                        .border(1.dp,Color.Gray,RoundedCornerShape(20))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (profile?.mobileNumber.isNullOrEmpty()) "Add your contact number" else "Contact: ${profile?.mobileNumber}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                    IconButton(onClick = { showEditDialog1 = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(20))
                        .border(1.dp,Color.Gray,RoundedCornerShape(20))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (profile?.emergencyContactName.isNullOrEmpty())
                    {
                        Text(
                            text = "Add an emergency contact",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                        )
                    }
                    else{
                        Column {
                            Text(
                                text = "Emergency Contact :",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(start = 8.dp, top=8.dp,bottom = 8.dp)
                            )
                            Text(
                                text = "Name : ${profile?.emergencyContactName}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(start = 8.dp, top=8.dp,bottom = 8.dp)
                            )
                            Text(
                                text = "Contact No : ${profile?.emergencyContactNumber}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(start = 8.dp, top=8.dp,bottom = 8.dp)
                            )
                        }
                    }

                    IconButton(onClick = { showEditDialog2 = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(20))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .border(1.dp,Color.Gray,RoundedCornerShape(20)),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Bloom Achievements",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                    IconButton(onClick = { navigateToAchievements(profile?.journalsCount?:0L) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "Edit",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                if (showEditDialog1) {
                    AlertDialog(
                        onDismissRequest = { showEditDialog1 = false },
                        title = { Text(text = "Edit Contact Number") },
                        text = {
                            Column {
                                TextField(
                                    value = newMobileNumber,
                                    onValueChange = { newMobileNumber = it },
                                    label = { Text(text = "Contact Number") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (newMobileNumber.isNotEmpty()) {
                                        viewModel.addMobileNumber(newMobileNumber)
                                        showEditDialog1 = false
                                    }
                                }
                            ) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showEditDialog1 = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                if(showEditDialog2){
                    AlertDialog(
                        onDismissRequest = { showEditDialog2 = false },
                        title = { Text(text = "Edit Emergency Contact") },
                        text = {
                            Column {
                                TextField(
                                    value = newContactName,
                                    onValueChange = { newContactName = it },
                                    label = { Text(text = "Contact Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = modifier.height(8.dp))
                                TextField(
                                    value = newContactNumber,
                                    onValueChange = { newContactNumber = it },
                                    label = { Text(text = "Contact Number") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (newContactName.isNotEmpty() && newContactNumber.isNotEmpty()) {
                                        viewModel.addEmergencyContact(newContactName,newContactNumber)
                                        showEditDialog2 = false
                                    }
                                }
                            ) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showEditDialog2 = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                Button(onClick = onSignOut, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Sign out")
                }
            }
        }
    }
}
