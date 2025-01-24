package com.bekircaglar.bluchat.presentation.chatlist.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.toUri
import com.bekircaglar.bluchat.domain.model.Chats
import com.bekircaglar.bluchat.utils.GROUP

@Composable
fun ChatElement(
    modifier: Modifier = Modifier,
    chats: Chats,
    lastMessageSenderName: String? = "",
    onClick: () -> Unit,
    isSelected: Boolean = false,
    currentUserId: String? = "",
) {

    val formattedMessageTime = "12:22"
    val chatType = chats.chatType

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

        AsyncImage(
            model = chats.chatImage.toUri(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(58.dp)
                .clip(if (chats.chatType == GROUP) MaterialTheme.shapes.large else CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (chatType == GROUP) chats.chatName else "${chats.name} ${chats.surname}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(bottom = 3.dp)
            )

            var myMessage = ""
            val chatLastMessage = chats.lastMessage?.message
            if (chats.chatType == GROUP && currentUserId == chats.lastMessage?.senderId) {
                myMessage = "You: $chatLastMessage"
            } else if (chats.chatType == GROUP && lastMessageSenderName != "" && chatLastMessage != "") {
                myMessage = "$lastMessageSenderName: $chatLastMessage"
            } else myMessage = "$chatLastMessage"

            Text(
                text = myMessage,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )


        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = formattedMessageTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }

        if (isSelected)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
    }

}
