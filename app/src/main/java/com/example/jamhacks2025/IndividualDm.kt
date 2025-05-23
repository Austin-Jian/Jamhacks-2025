package com.example.jamhacks2025

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable

@Composable
fun IndividualDm(navController: NavController, profileName: String, imageResId: Int) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<String>() }

    // Track if the team offer is shown
    var showTeamOffer by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffdc9e82))
            .padding(16.dp)
    ) {
        // Top Bar with Back Arrow
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .offset(x=24.dp, y=60.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(180f),
                    colorFilter = ColorFilter.tint(Color(0xff151e3f))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Chat with $profileName",
                color = Color(0xff151e3f),
                style = MaterialTheme.typography.displayLarge,
                fontSize = 24.sp,
                modifier = Modifier.offset(x = 20.dp, y=60.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$profileName seems to be a great fit for your team! Use /invite if you end up wanting to team!",
            color = Color(0xfff2f3d9),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 24.dp, end = 24.dp)
                .offset(y=12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            reverseLayout = true
        ) {
            // Regular messages
            items(messages.reversed()) { msg ->
                MessageBubble(content = msg)
            }

            // If the team offer is being shown, render the box
            if (showTeamOffer) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp)
                            .background(Color(0xff151e3f), RoundedCornerShape(10.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "You invited $profileName to your team!",
                            color = Color(0xfff2f3d9),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(bottom = 60.dp)
                        )

                        Button(
                            onClick = {
                                // Action on button press
                                responseMessage = "You accepted the team offer!"
                                TeamManager.addTeamMember(profileName, imageResId)
                                navController.popBackStack("Home", inclusive = false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffc16e70)),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {
                            Text("Join Team!", color = Color(0xfff2f3d9))
                        }
                    }
                }
            }
        }

        // ===== NEW: Response Message After Clicking Join/Decline =====
        if (responseMessage.isNotEmpty()) {
            Text(
                text = responseMessage,
                color = Color(0xfff2f3d9),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        // Input Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp)
                .padding(bottom = 60.dp)
                .background(Color(0xfff2f3d9), RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Type a message...", color = Color(0xffdc9e82)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                textStyle = TextStyle(color = Color(0xffdc9e82)),
                maxLines = 1
            )

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        // Add message to the list
                        if (messageText.lowercase() == "/invite") {
                            // If it's an invite, trigger the team offer without adding to messages
                            showTeamOffer = true
                        } else {
                            messages.add(messageText)
                        }

                        messageText = ""
                    }
                },
                modifier = Modifier
                    .offset(x = 0.dp)
                    .size(48.dp)
                    .background(Color(0xffc16e70), CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = "Send",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(0f),
                    colorFilter = ColorFilter.tint(Color(0xfff2f3d9))
                )
            }
        }
    }
}

@Composable
fun MessageBubble(content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(end=12.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            color = Color(0xfff2f3d9),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = content,
                modifier = Modifier.padding(12.dp),
                color = Color(0xff151e3f)
            )
        }
    }
}
