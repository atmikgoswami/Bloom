package com.example.bloom.journal.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bloom.R
import com.example.bloom.journal.data.models.Journal
import com.example.bloom.utils.formatTimestamp

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    journal: Journal
){
    Scaffold(
        topBar = {
            TopAppBar(modifier = modifier
                .height(60.dp),
                title = {
                    Box(
                        modifier = modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "Journal - ${formatTimestamp(journal.timestamp)}",
                            color = colorResource(id = R.color.black),
                            modifier = modifier
                                .padding(top = 8.dp, bottom = 8.dp, start=4.dp)
                                .align(Alignment.CenterStart),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
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
    )
    {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it))
        {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top=26.dp, start=16.dp, end=16.dp)
            ) 
            {
                LazyColumn(modifier = modifier.fillMaxSize())
                {
                    item{
                        Text(
                            text = "Title : ${journal.title}",
                            color = Color.Black,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
//                                textAlign = TextAlign.Center
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color.Gray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = journal.text,
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 18.sp,
                                //textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        } 
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun SamplePreview(){
    JournalScreen(navController = rememberNavController(), journal = Journal("1","Completed finishing the Bloom project","Today I incorporated a number of things fixed performance issues and made my app faster",1717594863835))
}