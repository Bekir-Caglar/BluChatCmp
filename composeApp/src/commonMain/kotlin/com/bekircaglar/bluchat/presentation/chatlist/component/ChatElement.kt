package com.bekircaglar.bluchat.presentation.chatlist.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.bekircaglar.bluchat.domain.model.Chats

@Composable
fun ChatElement(
    modifier: Modifier = Modifier,
    chat: Chats,
    lastMessageSenderName: String? = "",
    onClick: () -> Unit,
    isSelected: Boolean = false,
    onImageLoaded: () -> Unit,
    currentUserId: String? = "",
    messageType: String? = "",
) {
    val profileImage = chat.imageUrl
    val name = chat.name
    val surname = chat.surname
    val lastMessage = chat.lastMessage
    val messageTime = chat.messageTime
    val isOnline = chat.isOnline
    val lastMessageSender = chat.lastMessageSenderId
    val profileImageState = remember { mutableStateOf<AsyncImagePainter.State?>(null) }

    val formattedMessageTime = "12:!2"

//    val formattedMessageTime = remember(chat.messageTime) {
//        chat.messageTime?.let { it1 ->
//            formatMessageTime(
//                it1,
//                timeFormat,
//                dayOfWeekFormat,
//                dateFormat
//            )
//        }
//    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onClick()
            },
    ) {
        if (profileImageState.value is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(58.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }else{
            onImageLoaded()
            Box {
                AsyncImage(
                    model = profileImage,
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = if (chat.surname.isBlank()) modifier
                        .size(58.dp)
                        .clip(shape = MaterialTheme.shapes.large)
                    else modifier
                        .size(58.dp)
                        .clip(CircleShape),
                )
                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .align(Alignment.BottomEnd)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$name $surname",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(bottom = 3.dp)
            )
            var myMessage = ""
            if (lastMessage != null) {
                if (chat.surname == "" && currentUserId == lastMessageSender) {
                    myMessage = "You: $lastMessage"
                } else if (chat.surname == "" && lastMessageSenderName != "" && lastMessage != "") {
                    myMessage = "$lastMessageSenderName: $lastMessage"
                } else myMessage = "$lastMessage"
                Text(
                    text = myMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            if (formattedMessageTime != null) {
                Text(
                    text = formattedMessageTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }

        if (isSelected)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
    }
}
