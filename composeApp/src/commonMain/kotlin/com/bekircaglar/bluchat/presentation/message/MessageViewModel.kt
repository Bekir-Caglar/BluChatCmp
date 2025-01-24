package com.bekircaglar.bluchat.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Uri
import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.model.SignInUIState
import com.bekircaglar.bluchat.domain.model.TopBarUser
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.model.message.MessageType
import com.bekircaglar.bluchat.domain.usecase.message.CreateMessageRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.message.DeleteMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.EditMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetChatRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetMessageByIdUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetPinnedMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetStarredMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetUserFromChatIdUseCase
import com.bekircaglar.bluchat.domain.usecase.message.LoadInitialMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.LoadMoreMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.MarkMessageAsReadUseCase
import com.bekircaglar.bluchat.domain.usecase.message.ObserveGroupStatusUseCase
import com.bekircaglar.bluchat.domain.usecase.message.ObserveUserStatusInGroupUseCase
import com.bekircaglar.bluchat.domain.usecase.message.PinMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.SendMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.SetLastMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.StarMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UnPinMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UnStarMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UploadAudioUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UploadVideoUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.GetCurrentUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UploadImageUseCase
import com.bekircaglar.bluchat.utils.GROUP
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.UiState
import com.bekircaglar.bluchat.utils.data
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlin.random.Random

class MessageViewModel(
    private val auth: FirebaseAuth,
    private val getUserFromChatIdUseCase: GetUserFromChatIdUseCase,
    private val getUserUseCase: GetCurrentUserUseCase,
    private val createMessageRoomUseCase: CreateMessageRoomUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadInitialMessagesUseCase: LoadInitialMessagesUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
    private val getChatRoomUseCase: GetChatRoomUseCase,
    private val observeGroupStatusUseCase: ObserveGroupStatusUseCase,
    private val observeUserStatusInGroupUseCase: ObserveUserStatusInGroupUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val pinMessageUseCase: PinMessageUseCase,
    private val unPinMessageUseCase: UnPinMessageUseCase,
    private val getPinnedMessagesUseCase: GetPinnedMessagesUseCase,
    private val getStarredMessagesUseCase: GetStarredMessagesUseCase,
    private val starMessageUseCase: StarMessageUseCase,
    private val unStarMessageUseCase: UnStarMessageUseCase,
    private val markMessageAsReadUseCase: MarkMessageAsReadUseCase,
    private val uploadVideoUseCase: UploadVideoUseCase,
    private val setLastMessageUseCase: SetLastMessageUseCase,
    private val getMessageByIdUseCase: GetMessageByIdUseCase,
    private val uploadAudioUseCase: UploadAudioUseCase,

    ) : ViewModel() {

    val currentUser = auth.currentUser

    private val _userData = MutableStateFlow<Users?>(null)
    var userData = _userData.asStateFlow()

    private val _chatId = MutableStateFlow<String?>(null)
    var chatId = _chatId.asStateFlow()

    private val _notificationUsers = MutableStateFlow<Users?>(null)
    var notificationUsers = _notificationUsers.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private var lastKey: String? = null

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _uploadedImageUri = MutableStateFlow<Uri?>(null)
    val uploadedImageUri: StateFlow<Uri?> = _uploadedImageUri

    private val _uploadedVideoUri = MutableStateFlow<Uri?>(null)
    val uploadedVideoUri: StateFlow<Uri?> = _uploadedVideoUri

    private val _uploadedAudioUri = MutableStateFlow<Uri?>(null)
    val uploadedAudioUri: StateFlow<Uri?> = _uploadedAudioUri

    private val _uiState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _moreMessageState = MutableStateFlow<UiState>(UiState.Idle)
    val moreMessageState = _moreMessageState.asStateFlow()

    private val _pinnedMessages = MutableStateFlow<List<Message>>(emptyList())
    val pinnedMessages: StateFlow<List<Message>> = _pinnedMessages

    private val _starredMessages = MutableStateFlow<List<Message>>(emptyList())
    val starredMessages: StateFlow<List<Message>> = _starredMessages

    private val _isKickedOrGroupDeleted = MutableStateFlow(false)
    val isKickedOrGroupDeleted: StateFlow<Boolean> = _isKickedOrGroupDeleted

    private val _chatRoom = MutableStateFlow<ChatRoom?>(null)
    val chatRoom: StateFlow<ChatRoom?> = _chatRoom

    private val _topBarUser = MutableStateFlow<TopBarUser?>(null)
    val topBarUser: StateFlow<TopBarUser?> = _topBarUser

    private val _usersMessage = MutableStateFlow<Message?>(null)
    val usersMessage = _usersMessage.asStateFlow()






    fun updateChatId(chatId: String) {
        _chatId.value = chatId
    }

    fun clearChatId() {
        _chatId.value = null
    }

    fun createUserMessage() {
        _usersMessage.value = Message(
            messageId = Random.nextLong().toString(),
            senderId = currentUser?.uid,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    fun updateMessage(message: String) {
        _usersMessage.value = _usersMessage.value?.copy(message = message)
    }

    fun updateUploadedAudioUri(uri: Uri) {
        _usersMessage.value = _usersMessage.value?.copy(audioUrl = uri.toString())
    }

    fun updateAudioDuration(duration: Int) {
        _usersMessage.value = _usersMessage.value?.copy(audioDuration = duration)
    }

    fun updateUploadedVideoUri(uri: Uri) {
        _usersMessage.value = _usersMessage.value?.copy(videoUrl = uri.toString())
    }

    fun updateUploadedImageUri(uri: Uri) {
        _usersMessage.value = _usersMessage.value?.copy(imageUrl = uri.toString())
    }

    fun updateMessageType(messageType: String) {
        _usersMessage.value = _usersMessage.value?.copy(messageType = messageType)
    }

    fun updateReplyTo(messageId: String) {
        _usersMessage.value = _usersMessage.value?.copy(replyTo = messageId)
    }

    fun updateLocationName(locationName: String) {
        _usersMessage.value = _usersMessage.value?.copy(locationName = locationName)
    }

    fun updateLatitude(latitude: Double) {
        _usersMessage.value = _usersMessage.value?.copy(latitude = latitude)
    }

    fun updateLongitude(longitude: Double) {
        _usersMessage.value = _usersMessage.value?.copy(longitude = longitude)
    }

    fun clearUsersMessage() {
        _usersMessage.value = null
    }

    fun loadInitialMessages() = viewModelScope.launch {
        _chatId.value?.let {
            loadInitialMessagesUseCase(chatId = it).collect { result ->
                _messages.value = result.data ?: emptyList()
                lastKey = result.data?.lastOrNull()?.messageId
            }
        }
    }

    fun loadMoreMessages() = viewModelScope.launch {
        _chatId.value?.let {
            loadMoreMessagesUseCase(chatId = it, lastKey = lastKey ?: "").collect { result ->
                _messages.value += result.data?.reversed()?.distinctBy { it.messageId }
                    ?: emptyList()
                lastKey = result.data?.lastOrNull()?.messageId
            }
        }
    }


    fun sendMessage() = viewModelScope.launch {
        _chatId.value?.let { chatId ->
            _usersMessage.value?.let { message ->
                if (!message.messageId.isNullOrEmpty() && message.timestamp != 0L && !message.senderId.isNullOrEmpty() && !message.messageType.isNullOrEmpty()) {
                    if (message.messageType == MessageType.TEXT.toString() && message.message.isNullOrEmpty()) {
                    } else {
                        sendMessageUseCase(chatId = chatId, message = message).collect { result ->
                            _uiState.value = result
                        }
                    }
                }
            }
        }

    }


    fun createMessageRoom() = viewModelScope.launch {
        _chatId.value?.let {
            createMessageRoomUseCase(chatId = it).collect { result ->
                _uiState.value = result
            }
        }
    }

    fun getChatRoom() = viewModelScope.launch {
        _chatId.value?.let {
            getChatRoomUseCase(chatId = it).collect { result ->
                _chatRoom.value = result.data
                val chatRoom = result.data
                if (chatRoom?.useChatType() == GROUP) {
                    _topBarUser.value = TopBarUser(
                        chatName = chatRoom.useChatName(),
                        profileImageUrl = chatRoom.useChatImage()
                    )
                } else {
                    val otherUser = chatRoom?.usersList()?.firstOrNull { it != currentUser?.uid }
                    getUserById(otherUser ?: "")
                }
            }
        }
    }

    private fun getUserById(userId: String) = viewModelScope.launch {
        getUserUseCase.getUserById(userId).collect { result ->
            _topBarUser.value = TopBarUser(
                chatName = "${result.data?.name} ${result.data?.surname}",
                profileImageUrl = result.data?.profileImageUrl,
                isOnline = result.data?.isOnline,
            )
        }
    }

}