package com.bekircaglar.bluchat.presentation.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Uri
import coil3.toUri
import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.model.Chats
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.CreateChatRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.CreateGroupChatRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.GetUserChatListUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.SearchPhoneNumberUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.GetCurrentUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UploadImageUseCase
import com.bekircaglar.bluchat.utils.GROUP
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.data
import com.bekircaglar.bluchat.utils.isSuccess
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.random.Random

class ChatListViewModel(
    private val authUseCase: AuthUseCase,
    private val searchPhoneNumberUseCase: SearchPhoneNumberUseCase,
    private val getUserChatListUseCase: GetUserChatListUseCase,
    private val getUserUseCase: GetCurrentUserUseCase,
    private val createChatRoomUseCase: CreateChatRoomUseCase,
    private val createGroupChatRoomUseCase: CreateGroupChatRoomUseCase,
    private val auth: FirebaseAuth,
    private val uploadImageUseCase: UploadImageUseCase,
) : ViewModel() {

    val currentUserId = auth.currentUser?.uid

    private val _uiState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Users>>(emptyList())
    val searchResults: StateFlow<List<Users>> = _searchResults.asStateFlow()

    private val _chatList = MutableStateFlow<List<Chats>>(emptyList())
    val chatList = _chatList.asStateFlow()

    private val _chatRoomId = MutableStateFlow<String?>(null)
    val chatRoomId: StateFlow<String?> = _chatRoomId

    private var stateOfGroup = MutableStateFlow<QueryState<Unit>>(QueryState.Loading)
    private val stateOfPrivate = MutableStateFlow<QueryState<Unit>>(QueryState.Loading)

    private val _selectedUser = MutableStateFlow<Users?>(null)
    val selectedUser: StateFlow<Users?> = _selectedUser


    init {
        observeSearchQuery()
        if (stateOfPrivate.value == QueryState.Success(Unit) && stateOfGroup.value == QueryState.Success(Unit)) {
            _uiState.value = QueryState.Success(Unit)
        }
        getChatList()
    }


    fun onSearchQueryChange(newQuery: String) = viewModelScope.launch {
        _searchQuery.value = newQuery
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(300)
                .collect { query ->
                    searchPhoneNumberUseCase(query).collect {
                        when (it) {
                            is QueryState.Success -> {
                                _searchResults.value = it.data.let {
                                    it.filter { user -> user.uid != auth.currentUser?.uid }
                                }
                                _uiState.value = QueryState.Success(Unit)
                            }

                            is QueryState.Error -> {
                                _uiState.value = QueryState.Error(it.message)
                            }

                            is QueryState.Loading -> {
                                _uiState.value = QueryState.Loading
                            }

                            else -> {
                                _uiState.value = QueryState.Idle
                            }
                        }
                    }
                }
        }
    }

    fun createGroupChatRoom(
        groupMembers: List<String>,
        groupName: String,
        firebaseImageUrl: Uri?
    ) = viewModelScope.launch {
        val randomUUID = Random.nextLong().toString()
        val imageUrl = firebaseImageUrl
            ?: "https://firebasestorage.googleapis.com/v0/b/chatappbordo.appspot.com/o/def_user.png?alt=media&token=54d55dc5-4fad-415a-8b6f-d0f3b0619f31".toUri()

        if (currentUserId != null)
            createGroupChatRoomUseCase.invoke(
                currentUserId,
                groupMembers,
                randomUUID,
                groupName,
                imageUrl.toString()
            ).collect {
                if (it.isSuccess)
                    _chatRoomId.value = it.data
            }
    }

    fun createChatRoom(user: String) = viewModelScope.launch {
        val randomUUID = Random.nextLong().toString()
        if (currentUserId != null)
            createChatRoomUseCase.invoke(currentUserId, user, randomUUID).collect {
                when (it) {
                    is QueryState.Success -> {
                        _chatRoomId.value = it.data
                        _uiState.value = QueryState.Success(Unit)
                    }

                    is QueryState.Error -> {
                        _uiState.value = QueryState.Error()
                    }

                    is QueryState.Loading -> {
                        _uiState.value = QueryState.Loading
                    }

                    else -> {
                        _uiState.value = QueryState.Idle
                    }
                }
            }
    }

    private fun getChatList() = viewModelScope.launch {
        _uiState.value = QueryState.Loading
        getUserChatListUseCase.invoke().collect {
            when (it) {
                is QueryState.Success -> {
                    val myChats = it.data
                    val privateChats = mutableListOf<ChatRoom>()
                    myChats.forEach {
                        if (it.chatType == GROUP) {
                            val chat = Chats(
                                chatRoomId = it.useChatId(),
                                chatName = it.useChatName(),
                                chatType = it.useChatType(),
                                chatImage = it.useChatImage(),
                                lastMessage = it.useChatLastMessage(),
                            )

                            if (!_chatList.value.contains(chat))
                            _chatList.value += chat
                        } else {
                            privateChats += it
                        }
                    }
                    getUserById(privateChats)
                    stateOfGroup.value = QueryState.Success(Unit)
                }

                is QueryState.Error -> {
                    _uiState.value = QueryState.Error(it.message)
                }

                is QueryState.Loading -> {
                    _uiState.value = QueryState.Loading
                }

                else -> {
                    _uiState.value = QueryState.Idle
                }
            }
        }

    }


    private suspend fun getUserById(chats: List<ChatRoom>) {
        _uiState.value = QueryState.Loading
        val userIdList = chats.map { it.users?.first { user -> user != currentUserId } }

        getUserUseCase.getUsersByListOfIds(userIdList).collect { users ->
            when (users) {
                is QueryState.Success -> {
                    chats.forEach { chatRoom ->
                        users.data.forEach { user ->
                            if (chatRoom.users?.contains(user.uid) == true) {
                                val chat = Chats(
                                    chatRoomId = chatRoom.useChatId(),
                                    name = user.name ?: "",
                                    surname = user.surname ?: "",
                                    chatName = user.name + " " + user.surname,
                                    chatType = chatRoom.useChatType(),
                                    chatImage = user.profileImageUrl ?: "",
                                    lastMessage = chatRoom.useChatLastMessage(),
                                    isOnline = user.isOnline
                                )
                                if (!_chatList.value.contains(chat))
                                _chatList.value += chat
                            }
                        }
                    }
                    stateOfPrivate.value = QueryState.Success(Unit)
                }

                is QueryState.Error -> {
                    _uiState.value = QueryState.Error(users.message)
                }

                is QueryState.Loading -> {
                    _uiState.value = QueryState.Loading
                }

                else -> {
                    _uiState.value = QueryState.Idle
                }
            }
        }


    }

}