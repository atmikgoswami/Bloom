package com.example.bloom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.request.ImageRequest
import com.example.bloom.auth.domain.models.UserData
import com.example.bloom.journal.data.models.Journal
import com.example.bloom.journal.presentation.JournalViewModel
import kotlinx.coroutines.delay
import com.example.bloom.navigation.nav_routes
import com.example.bloom.navigation.Routes
import com.example.bloom.therapy.data.models.Doctors
import com.example.bloom.therapy.presentation.DoctorItem
import com.example.bloom.therapy.presentation.DoctorViewModel
import com.example.bloom.utils.formatTimestamp
import com.example.bloom.utils.toCamelCase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userData: UserData?,
    viewModel: JournalViewModel = hiltViewModel(),
    doctorViewModel: DoctorViewModel = hiltViewModel(),
    navigateToJournal: (Journal) -> Unit,
    onAppointmentClicked: (Doctors) -> Unit
) {
    val doctors by doctorViewModel.doctors.collectAsState()
    LaunchedEffect(Unit){
        doctorViewModel.loadDoctors()
        viewModel.loadJournals()
    }
    val journals by viewModel.journals.collectAsState()
    var selectedItemIndex1 by rememberSaveable {
        mutableIntStateOf(4)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.inverseOnSurface) {
                nav_routes.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = false,
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
                                contentDescription = item.title,
                            )
                        }
                    )
                }
            }
        }
    )
    {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                if (userData != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .padding(start = 12.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "\uD83D\uDC4B Hey ${userData.username}",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = TextStyle(
                                fontSize = 23.sp
                            ),
                            modifier = modifier.weight(1f)
                        )
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(userData.profilePictureUrl).crossfade(true).build() ,
                            contentDescription = "Profile picture",
                            modifier = modifier
                                .size(40.dp)
                                .clickable { navController.navigate(Routes.ProfileScreen.route) }
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = modifier.height(15.dp))
                AnimatedTextRow(
                    navController,
                    modifier,
                    listOf("Feeling Sad?", "Had a bad day?", "Thoughts on your mind?!")
                )
                Spacer(modifier = modifier.height(35.dp))

                if (journals.isNotEmpty()) {
                    Text(
                        text = "Your journals ✍\uFE0F",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = modifier.padding(start = 15.dp)
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp, start = 5.dp)
                    ) {
                        items(journals) { journal ->
                            HomeScreenJournals(journal, navigateToJournal)
                        }
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
                Text(
                    text = "Talk to Therapists",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = modifier.padding(start = 15.dp)
                )
                Spacer(modifier = modifier.height(16.dp))
            }
            items(doctors) { doctor ->
                DoctorItem(doctor = doctor, onAppointmentClicked)
            }
        }
    }
}

@Composable
fun AnimatedTextRow(navController: NavController,modifier: Modifier = Modifier,textList: List<String>, typingDelay: Long = 100L, pauseDelay: Long = 1000L) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var displayText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(true) }
    var isActive by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        onDispose { isActive = false }
    }
    LaunchedEffect(currentIndex, isTyping, isActive) {
        if (isActive) {
            val currentText = textList[currentIndex]
            if (isTyping) {
                for (i in 1..currentText.length) {
                    displayText = currentText.take(i)
                    delay(typingDelay)
                }
                delay(pauseDelay)
                isTyping = false
            } else {
                for (i in currentText.length downTo 0) {
                    displayText = currentText.take(i)
                    delay(typingDelay)
                }
                delay(pauseDelay)
                isTyping = true
                currentIndex = (currentIndex + 1) % textList.size
            }
        }
    }

    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clickable { navController.navigate(Routes.ChatBotScreen.route) }
        .height((110.dp))
        .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.mascot),
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Talk to Ember ✨", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }

            Spacer(modifier = modifier.height(8.dp))
            Text(text = displayText, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenJournals(journal:Journal, navigateToJournal:(Journal)->Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
            .clickable { navigateToJournal(journal) }
            .background(Color.White)
            .width(160.dp)
            .height(160.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = formatTimestamp(journal.timestamp).toCamelCase(),
                fontSize = 18.sp,
                //color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = journal.title.toCamelCase(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = journal.text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
                )
        }
    }
}
