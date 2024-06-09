package com.example.bloom.profile.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bloom.R
import com.example.bloom.navigation.Routes
import com.example.bloom.profile.data.models.Achievement
import com.example.bloom.profile.data.models.availableAchievements

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    journalsCount:Long
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
                            text = "Achievements",
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn{
                    items(availableAchievements){achievement->
                        AchievementItem(achievement = achievement, journalsCount = journalsCount)
                        Spacer(modifier = modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: Achievement,
    journalsCount: Long
)
{
    Row(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()
        .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
        .clip(RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically

    )  {
        Image(
            painter = painterResource(achievement.badgeImage),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = achievement.badgeName,style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            ),modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Unlocks on writing ${achievement.badgeCount} journals",style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            ),modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(4.dp))

            if(journalsCount >= achievement.badgeCount){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Congratulations for your achievement!",
                        textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                }
            }
            else{
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Locked!",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "locked", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}