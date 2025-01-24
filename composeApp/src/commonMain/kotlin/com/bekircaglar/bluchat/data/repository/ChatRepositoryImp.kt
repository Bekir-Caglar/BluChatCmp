package com.bekircaglar.bluchat.data.repository

import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.repository.ChatRepository
import com.bekircaglar.bluchat.utils.CHAT_COLLECTION
import com.bekircaglar.bluchat.utils.GROUP
import com.bekircaglar.bluchat.utils.PRIVATE
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.STORED_USERS
import com.bekircaglar.bluchat.utils.USER_COLLECTION
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class ChatRepositoryImp(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : ChatRepository {

    private val currentUser = auth.currentUser?.uid

    init {
        setUserOnlineStatus(auth.currentUser!!.uid)
    }



    override suspend fun searchContacts(query: String): Flow<QueryState<List<Users>>> = flow {
        val database = databaseReference.child(USER_COLLECTION)
        val currentUserId = auth.currentUser?.uid.orEmpty()
        val currentUserRef = databaseReference.child(USER_COLLECTION).child(currentUserId)

        runCatching {
            val contactListSnapshot = currentUserRef.child("contactsIdList").valueEvents.first()
            val contactIdList = contactListSnapshot.children.map { it.value<String>() }

            val allUsersSnapshot = database.valueEvents.first()
            val matchedUsers = allUsersSnapshot.children.mapNotNull { userSnapshot ->
                val user = userSnapshot.value<Users>()
                val phoneNumber = user.phoneNumber
                if (query.isBlank()) {
                    user.takeIf { userSnapshot.key in contactIdList }
                } else {
                    user.takeIf { phoneNumber?.contains(query) == true && userSnapshot.key in contactIdList }
                }
            }

            emit(QueryState.Success(matchedUsers))
        }.onFailure { error ->
            emit(QueryState.Error("Failed to search contacts: ${error.message}"))
        }
    }


    override suspend fun createChatRoom(
        user1: String, user2: String, chatRoomId: String
    ): Flow<QueryState<String>> = flow {
        val databaseRef = databaseReference.child(CHAT_COLLECTION)
        val chatType = PRIVATE
        runCatching {
            val chatRoomsSnapshot = databaseRef.valueEvents.first()

            val existingChatRoom = chatRoomsSnapshot.children.firstNotNullOfOrNull { snapshot ->
                snapshot.value<ChatRoom>().takeIf { chatRoom ->
                    chatRoom.users?.containsAll(
                        listOf(
                            user1,
                            user2
                        )
                    ) == true && chatRoom.chatType == chatType
                }?.let { snapshot.key }
            }

            if (existingChatRoom != null) {
                emit(QueryState.Success(existingChatRoom))
            } else {
                val newChatRoom = ChatRoom(
                    users = listOf(user1, user2),
                    chatId = chatRoomId,
                    chatType = chatType,
                    chatLastMessage = Message(),
                    chatCreatedAt = Clock.System.now().toEpochMilliseconds(),
                )
                databaseRef.child(chatRoomId).setValue(newChatRoom)
                emit(QueryState.Success(chatRoomId))
            }
        }.onFailure { error ->
            emit(QueryState.Error("Failed to create chat room: ${error.message}"))
        }
    }


    override suspend fun createGroupChatRoom(
        currentUserId: String,
        groupMembers: List<String>,
        chatId: String,
        groupName: String,
        groupImg: String
    ): Flow<QueryState<String>> = flow {
        val databaseRef = databaseReference.child(CHAT_COLLECTION)
        val chatType = GROUP

        val newGroupMembers = groupMembers.toMutableList().apply {
            add(currentUserId)
        }

        val chat = ChatRoom(
            users = newGroupMembers,
            chatId = chatId,
            chatName = groupName,
            chatImage = groupImg,
            chatType = chatType,
            chatAdminId = currentUser,
            chatCreatedAt = Clock.System.now().toEpochMilliseconds()
        )

        runCatching {
            databaseRef.child(chatId).setValue(chat)
            emit(QueryState.Success(chatId))
        }.onFailure { error ->
            emit(QueryState.Error("Failed to create chat room: ${error.message}"))
        }
    }


    override suspend fun getUsersChatList(): Flow<QueryState<List<ChatRoom>>> = flow {
        val databaseQuery = databaseReference.child(CHAT_COLLECTION).orderByChild(STORED_USERS)

        runCatching {
            val snapshot = databaseQuery.valueEvents.first()

            val chatList = snapshot.children.mapNotNull { chatSnapshot ->
                chatSnapshot.value<ChatRoom>().takeIf { chatRoom ->
                    chatRoom.users?.contains(currentUser) == true
                }
            }

            emit(QueryState.Success(chatList))
        }.onFailure { error ->
            emit(QueryState.Error("Failed to fetch chat list: ${error.message}"))
        }
    }

    private fun setUserOnlineStatus(userId: String) {
        val userStatusRef = databaseReference.child(USER_COLLECTION).child(userId).child("status")
        val lastSeenRef = databaseReference.child(USER_COLLECTION).child(userId).child("lastSeen")
        val connectedRef = databaseReference.child(".info/connected")

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            connectedRef.valueEvents.collect { snapshot ->
                val connected = snapshot.value<Boolean>()

                if (connected) {
                    userStatusRef.setValue(true)
                    userStatusRef.onDisconnect().setValue(false)
                    lastSeenRef.onDisconnect().setValue(Clock.System.now().toEpochMilliseconds())
                } else {
                    lastSeenRef.setValue(Clock.System.now().toEpochMilliseconds())
                }
            }
        }
    }


}