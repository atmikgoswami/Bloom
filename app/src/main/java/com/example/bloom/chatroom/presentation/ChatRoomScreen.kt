package com.example.bloom.chatroom.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bloom.R
import com.example.bloom.auth.domain.models.UserData
import com.example.bloom.chatroom.data.models.Message
import com.example.bloom.utils.formatTimestamp
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    roomId: String,
    roomName:String,
    userData: UserData?,
    anonymousName: String,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(roomId) {
        viewModel.setRoomId(roomId)
    }
    val text = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(modifier = modifier
                .height(60.dp),
                title = {
                    Box(
                        modifier = modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = roomName,
                            color = colorResource(id = R.color.black),
                            modifier = modifier
                                .heightIn(max = 48.dp).padding(top=8.dp, bottom=8.dp).align(Alignment.CenterStart),
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
    )
    {
        Box(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(it))
        {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = scrollState,
                    reverseLayout = true
                ) {

                    coroutineScope.launch {
                        scrollState.scrollToItem(0 )
                    }
                    items(messages.reversed()) { message ->
                        if (userData != null) {
                            ChatMessageItem(
                                message = message.copy(
                                    sentByCurrentUser
                                    = message.senderId == userData.userId
                                ), userData, viewModel =  viewModel, anonymousName
                            )
                        }
                    }
                }

                // Chat input field and send icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray, RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50)),
                        value = text.value,
                        onValueChange = { message -> text.value = message},
                        placeholder = {
                            Text(text = "Enter a message...")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )

                    )
                    IconButton(
                        onClick = {
                            // Send the message when the icon is clicked
                            if (text.value.isNotEmpty()) {
                                viewModel.sendMessage(text.value.trim(), anonymousName)
                                text.value = ""
                            }
                            viewModel.loadMessages()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send",tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message: Message,
                    userData: UserData?,
                    viewModel: MessageViewModel = hiltViewModel(),
                    anonymousName: String
) {
    var isLiked by remember { mutableStateOf(false) }
    var isLikeEnabled by remember { mutableStateOf(true) }

    if (userData != null && message.likedBy.contains(userData.userId)) {
        isLikeEnabled = false
        isLiked = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = if (message.sentByCurrentUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.sentByCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (isLikeEnabled) {
                                isLiked = true
                                isLikeEnabled = false
                                Log.d("Hello-bye", message.id)
                                viewModel.likeMessage(message.id)
                            }
                        }
                    )
                }
        ) {
            if(message.isRemoved) {
                Text(
                    text = "This message has been removed due to inappropriate content",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp),
                    fontStyle = FontStyle.Italic
                )
            }else{
                Text(
                    text = message.text,
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
        if (!message.sentByCurrentUser && isLiked) {
            Box(modifier = Modifier
                .background(
                    Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp))
            {
                Icon(
                    painter = rememberVectorPainter(Icons.Filled.ThumbUp),
                    contentDescription = "Liked",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(12.dp)
                )

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.senderName,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
        Text(
            text = formatTimestamp(message.timestamp),
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
