package com.example.bloom.journal.presentation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloom.R
import com.example.bloom.journal.data.models.Journal
import com.example.bloom.navigation.nav_routes
import com.example.bloom.utils.formatTimestamp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: JournalViewModel = hiltViewModel(),
    navigateToJournal: (Journal) -> Unit

){
    viewModel.loadJournals()
    val journals by viewModel.journals.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    var selectedItemIndex1 by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
             TopAppBar(modifier = modifier
                 .height(60.dp),
                 title = {
                     Box(
                         modifier = modifier.fillMaxSize(),
                     ) {
                         Text(
                             text = "Journals",
                             color = colorResource(id = R.color.black),
                             modifier = modifier
                                 .padding(top=8.dp, bottom = 8.dp, start=4.dp).align(Alignment.CenterStart),
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
                                 .padding(top=12.dp)
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
        floatingActionButton = {
            FloatingActionButton(
                modifier = modifier.padding(all = 20.dp),
                contentColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    showDialog = true
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
    )
    {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it))
        {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 25.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(journals) { journal ->
                    JournalItem(modifier,journal, navigateToJournal)
                }
            }

            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = true },
                    title = {
                        Row {
                            Text("Create a new journal",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = modifier.padding(top = 8.dp,start = 6.dp))
                            Spacer(modifier = modifier.width(8.dp))
                            IconButton(onClick = { showDialog = false }) {
                                Icon(painter = rememberVectorPainter(Icons.Default.Close), contentDescription = "close dialog")
                            }
                        }
                    },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = title,
                                onValueChange = { journalTitle -> title = journalTitle },
                                singleLine = true,
                                label = { Text(text = "Title")},
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            )
                            OutlinedTextField(
                                value = text,
                                onValueChange = { journalContent -> text = journalContent},
                                singleLine = false,
                                label = { Text(text = "Content")},
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .height(90.dp)
                            )
                        }

                    }, confirmButton = {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    if (text.isNotBlank()) {
                                        showDialog = false
                                        viewModel.addJournal(title, text)
                                        title = ""
                                        text = ""
                                    }
                                }
                            ) {
                                Text("Add")
                            }
                        }
                    },
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalItem(modifier: Modifier = Modifier,journal: Journal, navigateToJournal: (Journal) -> Unit){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clickable { navigateToJournal(journal) }
            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(8.dp)
        ) {
            Text(text = formatTimestamp(journal.timestamp).uppercase(Locale.getDefault()), fontSize = 16.sp, fontWeight = FontWeight.Normal,textAlign = TextAlign.Center)
            Spacer(modifier = modifier.height(8.dp))
            Text(text = journal.title,
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(text = journal.text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
                )
        }
    }
}