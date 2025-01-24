package com.bekircaglar.bluchat.presentation.chatlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
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
import bluchatkmp.composeapp.generated.resources.ic_notification
import com.bekircaglar.bluchat.navigation.Screens
import com.bekircaglar.bluchat.presentation.chatlist.component.BottomSheet
import com.bekircaglar.bluchat.presentation.chatlist.component.ChatAppFAB
import com.bekircaglar.bluchat.presentation.chatlist.component.ChatElement
import com.bekircaglar.bluchat.presentation.chatlist.component.NewChatBottomSheet
import com.bekircaglar.bluchat.presentation.chatlist.component.SearchTextField
import com.bekircaglar.bluchat.presentation.chatlist.component.ShimmerItem
import com.bekircaglar.bluchat.presentation.component.ChatAppBottomAppBar
import com.bekircaglar.bluchat.utils.isLoading
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
) {

    val viewModel: ChatListViewModel = koinViewModel()

    val currentUserId = viewModel.currentUserId
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val textFieldValue by viewModel.searchQuery.collectAsStateWithLifecycle()
    val chatRoomId by viewModel.chatRoomId.collectAsStateWithLifecycle()

    var isSearchActive by remember { mutableStateOf(false) }
    var addChatActive by remember { mutableStateOf(false) }
    var selectGroupUserDialog by remember { mutableStateOf(false) }
    var createGroupChatDialog by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var groupMembers by remember { mutableStateOf(emptyList<String>()) }


    val uiState = viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(chatRoomId) {
        if (chatRoomId != null)
            navController.navigate(Screens.MessageScreen.createRoute(chatId = chatRoomId!!))
    }

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
                IconButton(onClick = {
                    isSearchActive = !isSearchActive
                }
                ) {
                    Icon(
                        Icons.Default.Search, contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = {
                }) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_notification),
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            })
        },
        floatingActionButton = {
            ChatAppFAB(onClick = {
                isBottomSheetVisible = !isBottomSheetVisible
            })
        },
        bottomBar = { ChatAppBottomAppBar(navController = navController) }
    ) { contentPadding ->

        if (!uiState.value.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
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

                if (addChatActive) {
                    NewChatBottomSheet(
                        searchResults = searchResults,
                        textFieldValue = textFieldValue,
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                        onDismiss = {
                            addChatActive = false
                        },
                        onItemClick = { selectedUser ->
                            viewModel.createChatRoom(selectedUser.uid ?: "")
                            addChatActive = false
                        })
                }

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
                    items(chatList.sortedByDescending { it.lastMessage?.timestamp }) { chat ->
                        ChatElement(
                            chats = chat,
                            onClick = {
                                navController.navigate(Screens.MessageScreen.createRoute(chatId = chat.chatRoomId))
                            },
                            currentUserId = currentUserId,
                            isSelected = false,
                        )
                    }
                }
            }

        }
        if (uiState.value.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                repeat(10) {
                    ShimmerItem()
                }
            }
        }
    }

}