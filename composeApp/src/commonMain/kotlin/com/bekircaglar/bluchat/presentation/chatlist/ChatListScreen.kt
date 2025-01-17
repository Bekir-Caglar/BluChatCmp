package com.bekircaglar.bluchat.presentation.chatlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_facebook
import bluchatkmp.composeapp.generated.resources.ic_more
import com.bekircaglar.bluchat.domain.model.Chats
import com.bekircaglar.bluchat.presentation.chatlist.component.BottomSheet
import com.bekircaglar.bluchat.presentation.chatlist.component.ChatAppFAB
import com.bekircaglar.bluchat.presentation.chatlist.component.ChatElement
import com.bekircaglar.bluchat.presentation.chatlist.component.SearchTextField
import com.bekircaglar.bluchat.presentation.chatlist.component.ShimmerItem
import com.bekircaglar.bluchat.presentation.component.ChatAppBottomAppBar
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.UiState
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
) {
    val dummyChats = listOf(
        Chats(
            imageUrl = "https://randomuser.me/api/portraits/men/1.jpg",
            name = "John",
            surname = "Doe",
            lastMessage = "Hello",
            messageTime = Clock.System.now().toEpochMilliseconds(),
            isOnline = true,
            lastMessageSenderId = "1"
        ),
        Chats(
            imageUrl = "https://randomuser.me/api/portraits/men/2.jpg",
            name = "Jane",
            surname = "Doe",
            lastMessage = "Hi",
            messageTime = Clock.System.now().toEpochMilliseconds(),
            isOnline = false,
            lastMessageSenderId = "2"
        ),
    )

    val viewModel: ChatListViewModel = koinViewModel()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    var isSearchActive by remember { mutableStateOf(false) }
    var addChatActive by remember { mutableStateOf(false) }
    var selectGroupUserDialog by remember { mutableStateOf(false) }
    var createGroupChatDialog by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var groupMembers by remember { mutableStateOf(emptyList<String>()) }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                if (!isSearchActive)
                    Text(
                        text = "BluChat",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.background,
                actionIconContentColor = MaterialTheme.colorScheme.primary
            ), actions = {
                AnimatedVisibility(
                    visible = isSearchActive,
                    enter = fadeIn() + expandHorizontally(
                        expandFrom = Alignment.Start,
                        clip = false
                    ),
                    exit = shrinkHorizontally(
                        shrinkTowards = Alignment.Start,
                        clip = false
                    ) + fadeOut()
                ) {
                    SearchTextField(
                        query = searchText,
                        onQueryChange = { searchText = it },
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                IconButton(onClick = { isSearchActive = !isSearchActive }) {
                    Icon(
                        Icons.Default.Search, contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = {
                    viewModel.signOut()
                }) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_facebook),
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            })
        },
        floatingActionButton = { ChatAppFAB(onClick = { isBottomSheetVisible = !isBottomSheetVisible }) },
        bottomBar = { ChatAppBottomAppBar(navController = navController) }
    ) { contentPadding ->

        when (uiState.value) {
            is QueryState.Success -> {
//                if (selectGroupUserDialog) {
//                    SelectGroupMemberBottomSheet(
//                        searchResults = searchResults,
//                        textFieldValue = textFieldValue,
//                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
//                        onDismiss = {
//                            selectGroupUserDialog = false
//                        },
//                        onNext = {
//                            groupMembers = it
//                            createGroupChatDialog = true
//                            selectGroupUserDialog = false
//                        },
//                    )
//                }

//                if (addChatActive) {
//                    OpenChatBottomSheet(searchResults = searchResults,
//                        textFieldValue = textFieldValue,
//                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
//                        onDismiss = {
//                            addChatActive = false
//                        },
//                        navController = navController,
//                        onItemClick = {
//                            viewModel.createChatRoom(it.uid, navController)
//                            addChatActive = false
//                        })
//                }

                if (isBottomSheetVisible) {
                    BottomSheet(onDismiss = { isBottomSheetVisible = false }, onClicked = {
                        when (it) {
                            "New Chat" -> addChatActive = true
                            "Create Group Chat" -> selectGroupUserDialog = true
                        }
                    })
                }
//                if (createGroupChatDialog) {
//                    GroupChatBottomSheet(selectedUri = selectedImageUri,
//                        onDismissRequest = { createGroupChatDialog = false },
//                        onCreateGroupChat = { groupChatName ->
//                            viewModel.createGroupChatRoom(
//                                groupMembers, groupChatName, uploadedImageUri
//                            )
//                            createGroupChatDialog = false
//                        },
//                        isImageLoading = uploadImageState == UiState.Loading,
//                        onPermissionRequest = { permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES) })
//                }


                LazyColumn(
                    contentPadding = contentPadding,
                ) {
                    items(dummyChats.sortedByDescending { it.messageTime }) { chat ->
                        ChatElement(
                            chat = chat,
                            onClick = {
                            },
                            onImageLoaded = {},
                            currentUserId = "1",
                            messageType = "",
                            isSelected = false,
                        )
                    }
                }
            }

            is QueryState.Loading -> {

//                LoadingIndicator(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.primary,
//                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ){
                    repeat(10) {
                        ShimmerItem()
                    }
                }

            }

            is QueryState.Error -> {

            }

            else -> {}
        }
    }
}