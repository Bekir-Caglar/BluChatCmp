@file:OptIn(ExperimentalFoundationApi::class)

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.baseline_push_pin_24
import bluchatkmp.composeapp.generated.resources.double_tick
import coil3.compose.AsyncImage
import coil3.toUri
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.model.message.MessageType
import com.bekircaglar.bluchat.presentation.message.component.AudioMessageBubble
import com.bekircaglar.bluchat.presentation.message.component.convertTimestampToDate
import com.bekircaglar.bluchat.presentation.message.component.formatDuration
import com.bekircaglar.bluchat.utils.chatBubbleModifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

private val BubblePadding = 12.dp
private val BubbleShapeSent = RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
private val BubbleShapeReceived = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
private val BubbleColorReceived = Color(0xF7FFFFFF).copy(alpha = 0.6f)
private val TimestampFontSize = 12.sp
private val SenderNameFontSize = 14.sp
private val MessageFontSize = 16.sp

@Composable
fun ChatBubble(
    imageModifier: Modifier = Modifier,
    message: Message,
    isSentByMe: Boolean,
    timestamp: String,
    senderName: String,
    senderNameColor: Color,
    onImageClick: (String) -> Unit = {},
    onVideoClick: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onPinMessageClick: () -> Unit = {},
    onUnPinMessageClick: () -> Unit = {},
    onStarMessage: () -> Unit = {},
    onUnStarMessage: () -> Unit = {},
    onSwipeRight: (Message) -> Unit = {},
    replyMessage: Message? = null,
    replyMessageName: String? = null
) {
    val bubbleColorSent = MaterialTheme.colorScheme.primary
    var expanded by remember { mutableStateOf(false) }
    val timestamp = convertTimestampToDate(message.timestamp!!)

    MessageDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick,
        onPinMessageClick = onPinMessageClick,
        message = message,
        onUnPinMessageClick = onUnPinMessageClick,
        onStarMessage = onStarMessage,
        onUnStarMessage = onUnStarMessage
    )

    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    if (message.isDeleted) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .shadow(
                        elevation = 1.dp,
                        shape = if (isSentByMe) BubbleShapeSent else BubbleShapeReceived
                    )
                    .background(
                        if (isSentByMe) bubbleColorSent else Color.White,
                        shape = if (isSentByMe) BubbleShapeSent else BubbleShapeReceived
                    )
                    .padding(8.dp),

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = if (isSentByMe) Color.White.copy(0.7f) else Color(0xFF001F3F).copy(0.7f),
                    contentDescription = "Deleted",
                )
                Text(
                    text = "This message was deleted",
                    color = if (isSentByMe) Color.White.copy(0.7f) else Color(0xFF001F3F).copy(0.7f),
                    fontSize = MessageFontSize,
                    textAlign = if (isSentByMe) TextAlign.End else TextAlign.Start,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    } else
        Row(
            modifier = Modifier
                .chatBubbleModifier(isSentByMe) {
                    expanded = !expanded
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX.value > 300f) {
                                onSwipeRight(message)
                            }
                            coroutineScope.launch {
                                offsetX.animateTo(0f, tween(durationMillis = 300))
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                    }
                }
                .offset { IntOffset(offsetX.value.roundToInt(), 0) },
            horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
        ) {
            if (message.messageType == MessageType.AUDIO.toString()) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
                ) {
                    AudioMessageBubble(
                        audioUrl = message.useAudioUrl.toUri(),
                        audioDuration = message.useAudioDuration,
                        isIncoming = !isSentByMe
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
                ) {

                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .shadow(
                                elevation = 1.dp,
                                shape = if (isSentByMe) BubbleShapeSent else BubbleShapeReceived
                            )
                            .background(
                                if (isSentByMe) bubbleColorSent else BubbleColorReceived,
                                shape = if (isSentByMe) BubbleShapeSent else BubbleShapeReceived
                            ),
                        shape = if (isSentByMe) BubbleShapeSent else BubbleShapeReceived,
                        color = if (isSentByMe) bubbleColorSent else BubbleColorReceived
                    ) {
                        Column(modifier = Modifier.padding(BubblePadding)) {
                            replyMessage?.let {
                                ReplyMessage(replyMessageName, it)
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                            if (!isSentByMe) {
                                Text(
                                    text = senderName,
                                    color = senderNameColor,
                                    fontSize = SenderNameFontSize,
                                    textAlign = TextAlign.Start
                                )
                            }

                            Spacer(modifier = Modifier.size(8.dp))

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                when (message.messageType) {
                                    MessageType.TEXT.toString() -> TextMessage(
                                        message,
                                        isSentByMe
                                    )

                                    MessageType.IMAGE.toString() -> ImageMessage(
                                        message,
                                        isSentByMe,
                                        onImageClick, {
                                            if (isSentByMe) expanded = !expanded
                                        },
                                        imageModifier = imageModifier
                                    )

                                    MessageType.VIDEO.toString() -> VideoMessage(
                                        message,
                                        onVideoClick,
                                        isSentByMe
                                    )

                                    MessageType.LOCATION.toString() -> LocationMessage(
                                        isSentByMe = isSentByMe,
                                        message,
                                        { if (isSentByMe) expanded = !expanded })
                                }
                                MessageTimestamp(message, timestamp, isSentByMe)

                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun ReplyMessage(replyMessageName: String?, replyMessage: Message) {
    Row(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(end = 8.dp)
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = replyMessageName ?: "Anonymous",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            val formattedDuration = formatDuration(replyMessage.useAudioDuration * 1000)
            Text(
                text = if (replyMessage.messageType == MessageType.AUDIO.toString()) "Voice message 🎤 ($formattedDuration)"
                else replyMessage.useMessage ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TextMessage(message: Message, isSentByMe: Boolean) {
    Text(
        text = message.useMessage,
        color = if (isSentByMe) Color.White else Color(0xFF001F3F),
        fontSize = MessageFontSize,
        textAlign = TextAlign.Start
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageMessage(
    message: Message,
    isSentByMe: Boolean,
    onImageClick: (String) -> Unit,
    changeExpanded: () -> Unit,
    imageModifier: Modifier = Modifier
) {
    val imageUrl = message.useImageUrl
    AsyncImage(
        model = imageUrl,
        modifier = imageModifier
            .size(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                enabled = true,
                onClick = { onImageClick(imageUrl) },
                onLongClick = { if (isSentByMe) changeExpanded() }
            ),
        contentDescription = "Image Message",
        contentScale = ContentScale.Crop
    )

    if (message.useMessage.isNotEmpty()) {
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = message.useMessage,
            color = if (isSentByMe) Color.White else Color(0xFF001F3F),
            fontSize = MessageFontSize,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun VideoMessage(
    message: Message,
    onVideoClick: (String) -> Unit,
    isSentByMe: Boolean
) {
    val videoUrl = message.useVideoUrl
//    VideoThumbnailComposable(
//        context = context,
//        videoUrl = videoUrl,
//        onVideoClick = { onVideoClick(videoUrl) }
//    )
    if (message.useMessage.isNotEmpty()) {
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = message.useMessage,
            color = if (isSentByMe) Color.White else Color(0xFF001F3F),
            fontSize = MessageFontSize,
            textAlign = TextAlign.Start
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationMessage(
    isSentByMe: Boolean,
    message: Message,
    changeExpanded: () -> Unit
) {
    val latitude = message.useLatitude
    val longitude = message.useLongitude
    val mapsApiKey = "BuildConfig.GOOGLE_MAPS_KEY"

    val mapPhotoUrl = "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=15&size=400x400&markers=color:red%7C$latitude,$longitude&key=$mapsApiKey"
    val mapUrl = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

    AsyncImage(
        model = mapPhotoUrl,
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                enabled = true,
                onClick = {
                    // Open Google Maps
                },
                onLongClick = { if (isSentByMe) changeExpanded() }
            ),
        contentDescription = "Location Message",
        contentScale = ContentScale.Crop
    )

    if (message.useLocationName.isNotEmpty()) {
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = message.useLocationName,
            color = if (isSentByMe) Color.White else Color(0xFF001F3F),
            fontSize = MessageFontSize,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun MessageTimestamp(message: Message, timestamp: String, isSentByMe: Boolean) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        if (message.isEdited) {
            Text(
                text = "Edited",
                color = Color.White,
                fontSize = TimestampFontSize,
                textAlign = TextAlign.End,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        }
        Text(
            text = timestamp,
            color = if (isSentByMe) Color.White.copy(0.8f) else Color.Gray,
            fontSize = TimestampFontSize,
            textAlign = TextAlign.End,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        if (isSentByMe) {
            Icon(
                painter = painterResource(Res.drawable.double_tick),
                contentDescription = "Tick",
                tint = if (message.isRead) Color.Green else Color.LightGray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun MessageDropdownMenu(
    expanded: Boolean,
    message: Message,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onPinMessageClick: () -> Unit,
    onUnPinMessageClick: () -> Unit,
    onStarMessage: () -> Unit,
    onUnStarMessage: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.large)
            .padding(16.dp)
            .width(150.dp)
    ) {
        DropdownMenuItem(
            enabled = true,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.background),
            onClick = {
                onEditClick()
                onDismissRequest()
            },
            text = {
                Row(

                ) {
                    Text(text = "Edit")
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }

            },
        )
        DropdownMenuItem(
            enabled = true,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.background),
            onClick = {
                if (message.pinned == true) {
                    onUnPinMessageClick()
                } else {
                    onPinMessageClick()
                }
                onDismissRequest()
            },
            text = {
                Row {
                    Text(
                        text = if (message.pinned == true) "Unpin message" else "Pin message"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(Res.drawable.baseline_push_pin_24),
                        contentDescription = "Delete",
                    )
                }
            }
        )

        DropdownMenuItem(
            enabled = true,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.background),
            onClick = {
                if (message.starred == true) {
                    onUnStarMessage()
                } else {
                    onStarMessage()
                }
                onDismissRequest()
            },
            text = {
                Row {
                    Text(
                        text = if (message.starred == true) "Unstar message" else "Star message"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Star",
                    )
                }
            }
        )

        DropdownMenuItem(
            enabled = true,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.background),
            onClick = {
                onDeleteClick()
                onDismissRequest()
            }, text = {
                Row {
                    Text(
                        text = "Delete",
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        )


    }

}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun VideoThumbnailComposable(
//    context: Context,
//    videoUrl: String,
//    onVideoClick: (String) -> Unit,
//    size: Dp = 200.dp,
//    isShapeShouldSquare: Boolean = true
//) {
//    val imageLoader = remember { ImageLoader(context) }
//    var videoThumbnail by remember { mutableStateOf<Bitmap?>(null) }
//
//    LaunchedEffect(videoUrl) {
//        videoThumbnail = withContext(Dispatchers.IO) {
//            imageLoader.getVideoThumbnail(context, videoUrl)
//        }
//    }
//
//    videoThumbnail?.let { bitmap ->
//        Box {
//            Image(
//                painter = rememberImagePainter(bitmap),
//                contentDescription = "Video Thumbnail",
//                modifier = if (isShapeShouldSquare) {
//                    Modifier
//                        .size(size)
//                        .clip(shape = RoundedCornerShape(12.dp))
//                        .combinedClickable(
//                            enabled = true,
//                            onClick = {
//                                onVideoClick(videoUrl)
//                            },
//                            onLongClick = {}
//                        )
//                } else {
//                    Modifier
//                        .fillMaxWidth()
//                },
//                contentScale = ContentScale.Crop
//            )
//
//            Icon(
//                imageVector = Icons.Default.PlayArrow,
//                contentDescription = "play button",
//                modifier = Modifier
//                    .background(color = Color.Black.copy(alpha = 0.3f), shape = CircleShape)
//                    .size((size / 3))
//                    .align(alignment = Alignment.Center)
//            )
//
//
//        }
//    } ?: run {
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .size(size)
//                .background(Color.Black, shape = RoundedCornerShape(12.dp))
//        ) {
//            CircularProgressIndicator(modifier = Modifier.size(size / 4))
//
//        }
//    }
//}