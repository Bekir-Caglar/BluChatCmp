package com.bekircaglar.bluchat.presentation.message

import ChatBubble
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.baseline_push_pin_24
import bluchatkmp.composeapp.generated.resources.ic_camera
import bluchatkmp.composeapp.generated.resources.ic_location
import bluchatkmp.composeapp.generated.resources.ic_photos
import bluchatkmp.composeapp.generated.resources.ic_video_camera_media
import bluchatkmp.composeapp.generated.resources.wp_background
import bluchatkmp.composeapp.generated.resources.wp_dark
import coil3.compose.AsyncImage
import com.bekircaglar.bluchat.AppContext
import com.bekircaglar.bluchat.domain.model.SheetOption
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.model.message.MessageType
import com.bekircaglar.bluchat.navigation.Screens
import com.bekircaglar.bluchat.presentation.component.ChatAppTopBar
import com.bekircaglar.bluchat.presentation.message.component.ImageSendBottomSheet
import com.bekircaglar.bluchat.presentation.message.component.MessageAlertDialog
import com.bekircaglar.bluchat.presentation.message.component.MessageBottomBar
import com.bekircaglar.bluchat.presentation.message.component.MessageExtraBottomSheet
import com.bekircaglar.bluchat.presentation.message.component.MessageTextField
import com.bekircaglar.bluchat.presentation.message.component.formatDuration
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.UiState
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun MessageScreen(
    navController: NavController,
    chatId: String,
) {


    val viewModel: MessageViewModel = koinViewModel()
    val context = AppContext

    val topBarUser by viewModel.topBarUser.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val uploadedImage by viewModel.uploadedImageUri.collectAsStateWithLifecycle()
    val uploadedVideo by viewModel.uploadedVideoUri.collectAsStateWithLifecycle()
    val selectedImage by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val uploadedAudioUri by viewModel.uploadedAudioUri.collectAsStateWithLifecycle()
    val isKickedOrGroupDeleted by viewModel.isKickedOrGroupDeleted.collectAsState()
    val currentUser = viewModel.currentUser
    var messageText by remember { mutableStateOf("") }
    var bottomSheetState by remember { mutableStateOf(false) }
    var imageSendDialogState by remember { mutableStateOf(false) }
    var chatMessage by remember { mutableStateOf("") }
    var editedMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    var pageNumber by remember { mutableStateOf(1) }

    var emojiState by remember { mutableStateOf(false) }

    var selectedMessageForDeletion by remember { mutableStateOf<Message?>(null) }
    var selectedMessageForPin by remember { mutableStateOf<Message?>(null) }
    var selectedMessageForEdit by remember { mutableStateOf<Message?>(null) }
    var selectedMessageForReply by remember { mutableStateOf<Message?>(null) }

    var videoUploadState by remember { mutableStateOf(false) }
    var imageUploadState by remember { mutableStateOf(false) }
    var replyState by remember { mutableStateOf(false) }


    val pinnedMessages by viewModel.pinnedMessages.collectAsStateWithLifecycle()

    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val moreMessageState by viewModel.moreMessageState.collectAsStateWithLifecycle()

    val groupedMessages = messages.groupBy { message ->
        convertTimestampToDay(message.timestamp!!)
    }


    LaunchedEffect(chatId){
        viewModel.updateChatId(chatId)
        viewModel.createMessageRoom()
        viewModel.getChatRoom()
        viewModel.loadInitialMessages()
    }

    val startPagination by remember {
        derivedStateOf {
            val lastIndex = (pageNumber * 15) - listState.firstVisibleItemIndex
            val totalItemCount = messages.size
            lastIndex >= totalItemCount - 3
        }
    }
    LaunchedEffect(startPagination) {
        if (startPagination && messages.size >= 15) {
            viewModel.loadMoreMessages()
            pageNumber++
        }
    }

//    val groupedMessages = messages.reversed().groupBy { message ->
//        convertTimestampToDay(message.timestamp!!)
//    }

//    LaunchedEffect(key1 = chatId) {
//        viewModel.getChatRoom(chatId)
//        viewModel.createMessageRoom(chatId)
//        viewModel.loadInitialMessages(chatId)
//    }

//    val startPagination by remember {
//        derivedStateOf {
//            val lastIndex = (pageNumber * 15) - listState.firstVisibleItemIndex
//            val totalItemCount = messages.size
//            lastIndex >= totalItemCount - 3
//        }
//    }
//    LaunchedEffect(startPagination) {
//        if (startPagination && messages.size >= 15) {
//            viewModel.loadMoreMessages(moreLastKey = messages.lastOrNull()?.messageId, chatId)
//            pageNumber++
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        val groupId = chatId
//        val userId = currentUser.uid
//        viewModel.observeGroupAndUserStatus(groupId, userId)
//    }
//
//    LaunchedEffect(isKickedOrGroupDeleted) {
//        if (isKickedOrGroupDeleted) {
//            navController.navigate(Screens.ChatListScreen.route) {
//                popUpTo(Screens.MessageScreen.route) { inclusive = true }
//            }
//        }
//    }

    Scaffold(
        topBar = {
            ChatAppTopBar(title = {
                if (topBarUser != null)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screens.ChatInfoScreen.createRoute(chatId))
                        }
                        .padding(start = 8.dp)
                ) {
                    AsyncImage(
                        model = topBarUser?.profileImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background, CircleShape)
                    )

                    Column {
                        Text(
                            text = if (topBarUser?.chatName.isNullOrEmpty()) topBarUser?.name + " " + topBarUser?.surname else topBarUser?.chatName!!,
                            modifier = Modifier
                                .padding(start = 8.dp),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                        if (topBarUser?.isOnline == true) {
                            Text(
                                text = "Online",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }


            }, navigationIcon = Icons.Default.KeyboardArrowLeft,
                onNavigateIconClicked = {
                    navController.navigate(Screens.ChatListScreen.route){
                        popUpTo(Screens.MessageScreen.route) { inclusive = true }
                    }
                },
                actionIcon = Icons.Outlined.Star,
                onActionIconClicked = {
                    navController.navigate(Screens.StarredMessagesScreen.createRoute(chatId))
                },
                onActionIcon2Clicked = {
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.background(Color.Transparent)
            ) {
                if (replyState) {
                    var senderName by remember { mutableStateOf("") }
                    selectedMessageForReply?.senderId?.let {
//                        viewModel.getUserNameFromUserId(
//                            it, onResult = {
//                                senderName = it
//                            })
                    }
                    selectedMessageForReply?.let {
                        Reply(
                            messageSenderName = senderName,
                            message = it,
                            onDismiss = {
                                replyState = false
                                selectedMessageForReply = null
                            })
                    }
                }
                MessageBottomBar(
                    onAttachClicked = {
                        bottomSheetState = true
                    },
                    onSendMessage = { message ->
                        viewModel.createUserMessage()
                        viewModel.updateMessage(message)
                        viewModel.updateMessageType(MessageType.TEXT.toString())
                        viewModel.sendMessage()
                        viewModel.clearUsersMessage()
                        replyState = false
                        selectedMessageForReply = null
                    },
                    onCameraClicked = {
                    },
                    onSendAudio = { audioPath, audioDuration ->
                    }
                )


            }

        }

    ) {
        Box(modifier = Modifier.padding(it)) {
            if (screenState is QueryState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                    )
                }
            } else if (screenState is QueryState.Success) {
                LaunchedEffect(Unit) {
                    listState.scrollToItem(messages.lastIndex + 1)

                }
                if (messages.isNotEmpty()) {
                    val lastMessageId = messages.first().messageId
                    LaunchedEffect(lastMessageId) {
                        listState.scrollToItem(messages.lastIndex + 1)

                    }
                }
                LaunchedEffect(listState.isScrollInProgress) {
//                    listState.layoutInfo.visibleItemsInfo.forEach { visibleItem ->
//                        val myMessage = messages.find {
//                            visibleItem.key == it.messageId
//                        }
//                        if (myMessage?.read == false && myMessage.senderId != currentUser.uid)
//                            viewModel.markMessageAsRead(visibleItem.key.toString(), chatId)
//
//                    }
                }

                if (imageSendDialogState) {
                    ImageSendBottomSheet(
                        imageResId = uploadedImage!!,
                        onSend = { imageResId, message ->
//                            viewModel.sendMessage(
//                                message = message,
//                                chatId = chatId,
//                                imageUrl = imageResId,
//                                messageType = MessageType.IMAGE.toString(),
//                            )
                            imageSendDialogState = false
                        },
                        onDismiss = {
                            imageSendDialogState = false
                        }
                    )
                }

//                LaunchedEffect(uploadedImage) {
//                    imageUploadState = false
//                    if (uploadedImage != null) {
//                        imageSendDialogState = true
//                    }
//                }
//
//                LaunchedEffect(uploadedVideo) {
//                    videoUploadState = false
//                    if (uploadedVideo != null) {
//                        val encodedVideoUrl = URLEncoder.encode(
//                            uploadedVideo.toString(),
//                            StandardCharsets.UTF_8.toString()
//                        )
//                        navController.navigate(
//                            Screens.SendTakenPhotoScreen.createRoute(
//                                encodedVideoUrl,
//                                chatId
//                            )
//                        )
//                    }
//                }

                if (bottomSheetState) {
                    MessageExtraBottomSheet(
                        onDismiss = { bottomSheetState = false },
                        onClicked = { option ->
//                            when (option) {
//                                "Photos" -> {
//                                    permissionLauncherForGallery.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
//                                }
//
//                                "Video" -> {
//                                    permissionLauncherForVideo.launch(android.Manifest.permission.READ_MEDIA_VIDEO)
//                                }
//
//                                "Camera" -> {
//                                    permissionLauncherForCamera.launch(android.Manifest.permission.CAMERA)
//                                }
//
//                                "Location" -> {
//                                    navController.navigate(
//                                        Screens.MapScreen.createRoute(
//                                            chatId
//                                        )
//                                    )
//                                }
//                            }
                        },
                        myList = listOf(
                            SheetOption("Photos", Res.drawable.ic_photos),
                            SheetOption("Video", Res.drawable.ic_video_camera_media),
                            SheetOption("Camera", Res.drawable.ic_camera),
                            SheetOption("Location", Res.drawable.ic_location),
//                            SheetOption("Contact", R.drawable.ic_user_square),
                        )
                    )
                }

                if (messages.isNotEmpty()) {
                    Column(modifier = Modifier.padding(it)) {
                        if (moreMessageState is UiState.Loading) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    val lastPinnedMessage = pinnedMessages.lastOrNull()
                    Column {
                        if (lastPinnedMessage != null && !lastPinnedMessage.isDeleted) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(MaterialTheme.shapes.medium)
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = 0.3f
                                                )
                                            )
                                    ) {
                                        Icon(
                                            painter = painterResource(resource = Res.drawable.baseline_push_pin_24),
                                            contentDescription = null,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                    val formattedDuration = formatDuration(
                                        lastPinnedMessage?.useAudioDuration?.times(1000)
                                            ?: 0
                                    )
                                    val voiceMessage =
                                        if (lastPinnedMessage?.messageType == MessageType.AUDIO.toString()) "Voice message 🎤 ($formattedDuration)"
                                        else ""

                                    Text(
                                        text = when (lastPinnedMessage?.messageType) {

                                            MessageType.TEXT.toString() -> if (lastPinnedMessage.message.isNullOrEmpty()) "Text message" else lastPinnedMessage.useMessage
                                            MessageType.IMAGE.toString() -> if (lastPinnedMessage.message.isNullOrEmpty()) "Image 🏞️" else lastPinnedMessage.useMessage
                                            MessageType.VIDEO.toString() -> if (lastPinnedMessage.message.isNullOrEmpty()) "Video 🎥" else lastPinnedMessage.useMessage
                                            MessageType.LOCATION.toString() -> if (lastPinnedMessage.message.isNullOrEmpty()) "Location 🗺️" else lastPinnedMessage.useMessage
                                            MessageType.AUDIO.toString() -> if (lastPinnedMessage.message.isNullOrEmpty()) voiceMessage else lastPinnedMessage.useMessage
                                            else -> ""

                                        },
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .padding(start = 8.dp)
                                    )
                                }

                                when (lastPinnedMessage?.messageType) {
                                    MessageType.IMAGE.toString() -> {
                                        AsyncImage(
                                            model = lastPinnedMessage.useImageUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(MaterialTheme.shapes.medium)
                                        )
                                    }

                                    MessageType.VIDEO.toString() -> {
//                                        VideoThumbnailComposable(
//                                            context = context,
//                                            size = 50.dp,
//                                            videoUrl = lastPinnedMessage.useVideoUrl,
//                                            onVideoClick = {}
//                                        )
                                    }

                                    MessageType.LOCATION.toString() -> {
                                        val latitude = lastPinnedMessage.useLatitude
                                        val longitude = lastPinnedMessage.useLongitude
                                        val mapsApiKey = ""

                                        val mapUrl =
                                            "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=15&size=400x400&markers=color:red%7C$latitude,$longitude&key=$mapsApiKey"
                                        AsyncImage(
                                            model = mapUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(MaterialTheme.shapes.medium)
                                        )
                                    }


                                }
                            }

                        }
                        LazyColumn(
                            state = listState,
                            reverseLayout = false,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary.copy(0.2f))
                                .paint(
                                    painter = if (isSystemInDarkTheme()) {
                                        painterResource(resource = Res.drawable.wp_dark)
                                    } else {
                                        painterResource(resource = Res.drawable.wp_background)
                                    },
                                    contentScale = ContentScale.FillBounds
                                )
                        ) {
                            groupedMessages.forEach { (date, messagesForDate) ->
                                stickyHeader {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.surface.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = date,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                itemsIndexed(
                                    messagesForDate,
                                    key = { _, message ->
                                        message.messageId ?: 0
                                    }) { _, message ->
                                    if (message != null) {
                                        val timestamp =
                                            convertTimestampToDate(message.timestamp!!)
                                        val senderId = message.senderId

                                        var senderName by remember { mutableStateOf("") }
//                                        LaunchedEffect(senderId) {
//                                            viewModel.getUserNameFromUserId(senderId!!) { name ->
//                                                senderName = name
//                                            }
//                                        }
                                        var replyedMessage by remember {
                                            mutableStateOf<Message?>(
                                                null
                                            )
                                        }
                                        if (message.replyTo != "") {
                                            LaunchedEffect(replyedMessage) {
//                                                viewModel.getMessageById(
//                                                    message.replyTo.toString(),
//                                                    chatId,
//                                                    onResult = {
//                                                        replyedMessage = it
//                                                    }
//                                                )
                                            }
                                        }

                                        var senderReplyName by remember { mutableStateOf("") }
                                        LaunchedEffect(replyedMessage) {
                                            replyedMessage?.senderId?.let { it1 ->
//                                                viewModel.getUserNameFromUserId(it1) { name ->
//                                                    senderReplyName = name
//                                                }
                                            }
                                        }

//                                        val senderNameColor = viewModel.getUserColor(senderId!!)
                                        message.messageType?.let { messageType ->
                                            ChatBubble(
                                                message = message,
                                                isSentByMe = message.senderId == currentUser?.uid,
                                                timestamp = timestamp,
                                                senderName = senderName,
                                                senderNameColor = Color.Red,
                                                onImageClick = { imageUrl ->
//                                                    val encode = URLEncoder.encode(
//                                                        imageUrl,
//                                                        StandardCharsets.UTF_8.toString()
//                                                    )
//                                                    navController.navigate(
//                                                        Screens.ImageScreen.createRoute(
//                                                            encode
//                                                        )
//                                                    )
                                                },
                                                onVideoClick = { videoUrl ->
//                                                    val intent = Intent(
//                                                        context,
//                                                        VideoPlayerActivity::class.java
//                                                    ).apply {
//                                                        putExtra("videoUri", videoUrl)
//                                                    }
//                                                    context.startActivity(intent)
                                                },
                                                onEditClick = {
                                                    selectedMessageForEdit = message
                                                },
                                                onDeleteClick = {
                                                    selectedMessageForDeletion = message
                                                },
                                                onPinMessageClick = {
//                                                    viewModel.pinMessage(message, chatId)
                                                },
                                                onUnPinMessageClick = {
//                                                    viewModel.unPinMessage(message, chatId)
                                                },
                                                onStarMessage = {
//                                                    viewModel.starMessage(message, chatId)
                                                },
                                                onUnStarMessage = {
//                                                    viewModel.unStarMessage(message, chatId)
                                                },
                                                onSwipeRight = {
                                                    selectedMessageForReply = it
                                                    replyState = true
                                                },
                                                replyMessage = replyedMessage,
                                                replyMessageName = senderReplyName
                                            )
                                        }

                                    }

                                }
                            }
                        }
                    }


                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Let's start chatting",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                if (selectedMessageForEdit != null && selectedMessageForEdit?.senderId == currentUser?.uid) {
                    Dialog(onDismissRequest = { selectedMessageForEdit = null }) {
                        Column {
                            ChatBubble(
                                message = selectedMessageForEdit!!,
                                isSentByMe = selectedMessageForEdit!!.senderId == currentUser?.uid,
                                timestamp = convertTimestampToDate(selectedMessageForEdit!!.timestamp!!),
                                senderName = "",
                                onImageClick = { },
                                senderNameColor = Color.Transparent,
                                onEditClick = { },
                                onDeleteClick = { },
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = MaterialTheme.shapes.medium
                                    )
                            ) {
                                MessageTextField(
                                    searchText = editedMessage,
                                    onSearchTextChange = { newText ->
                                        editedMessage =
                                            newText.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                                    },
                                    onSend = {
//                                        viewModel.editMessage(
//                                            messageId = selectedMessageForEdit!!.messageId!!,
//                                            chatId = chatId,
//                                            message = editedMessage
//                                        )
                                        selectedMessageForEdit = null
                                    },
                                    placeholderText = "Edit your message",
                                    modifier = Modifier
                                        .width(250.dp)
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = {
//                                        viewModel.editMessage(
//                                            messageId = selectedMessageForEdit!!.messageId!!,
//                                            chatId = chatId,
//                                            message = editedMessage
//                                        )
                                        selectedMessageForEdit = null

                                    }, modifier = Modifier.padding(end = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Send edited message",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
                if (selectedMessageForDeletion != null && selectedMessageForDeletion?.senderId == currentUser?.uid) {
                    MessageAlertDialog(
                        message = selectedMessageForDeletion!!,
                        onDismiss = { selectedMessageForDeletion = null },
                        onConfirm = {
//                            viewModel.deleteMessage(
//                                chatId = chatId,
//                                messageId = selectedMessageForDeletion!!.messageId!!
//                            )
//                            viewModel.unPinMessage(selectedMessageForDeletion!!, chatId)
                            selectedMessageForDeletion = null
                        },
                    )
                }
            } else if (
                screenState is QueryState.Error
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "An error occurred",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }
        }
    }
    if (videoUploadState || imageUploadState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun Reply(messageSenderName: String, message: Message, onDismiss: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary.copy(0.15f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(50.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(end = 8.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = messageSenderName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            when (message.messageType) {
                MessageType.TEXT.toString() -> {
                    Text(
                        text = message.useMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                MessageType.IMAGE.toString() -> {
                    Text(
                        text = message.message ?: "Image 🏞️",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }

                MessageType.VIDEO.toString() -> {
                    Text(
                        text = message.message ?: "Video 🎥",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                MessageType.LOCATION.toString() -> {
                    Text(
                        text = message.message ?: "Location 🗺️",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                MessageType.AUDIO.toString() -> {
                    val formattedDuration = formatDuration(message.useAudioDuration * 1000)
                    Text(
                        text = "Voice message 🎤 (${formattedDuration})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }


        IconButton(onClick = { onDismiss() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove quoted message",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


fun convertTimestampToDate(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)

    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val formattedTime = "${dateTime.hour.toString().padStart(2, '0')}:${
        dateTime.minute.toString().padStart(2, '0')
    }"

    return formattedTime
}

fun convertTimestampToDay(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)

    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val formattedDate = "${dateTime.year}-${
        dateTime.monthNumber.toString().padStart(2, '0')
    }-${dateTime.dayOfMonth.toString().padStart(2, '0')}"

    return formattedDate
}