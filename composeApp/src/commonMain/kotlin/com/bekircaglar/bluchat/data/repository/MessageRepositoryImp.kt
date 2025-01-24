package com.bekircaglar.bluchat.data.repository

import coil3.Uri
import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.model.message.Messages
import com.bekircaglar.bluchat.domain.repository.MessageRepository
import com.bekircaglar.bluchat.utils.CHAT_COLLECTION
import com.bekircaglar.bluchat.utils.MESSAGE_COLLECTION
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.utils.STORED_MESSAGES
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MessageRepositoryImp(
    private val auth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
    private val firebaseDataSource: FirebaseDataSource
) : MessageRepository {

    private val currentUserId = auth.currentUser?.uid

    override suspend fun createMessageRoom(chatId: String): Flow<QueryState<Unit>> = flow {
        kotlin.runCatching {
            val chatRoomRef = databaseReference.child(MESSAGE_COLLECTION).valueEvents.first()

            val roomExists = chatRoomRef.children.any { it.key == chatId }

            if (!roomExists) {
                val messageRoom = Messages(id = chatId, messages = emptyList())
                databaseReference.child(MESSAGE_COLLECTION).child(chatId).setValue(messageRoom)
                emit(QueryState.Success(Unit))
            } else {
                emit(QueryState.Success(Unit))
            }
        }.onFailure { e ->
            emit(QueryState.Error(e.message))
        }
    }


    override suspend fun deleteMessage(
        chatId: String,
        messageId: String
    ): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(chatId: String, message: Message): Flow<QueryState<Unit>> =
        flow {
            kotlin.runCatching {
                val messageRef =
                    databaseReference.child(MESSAGE_COLLECTION).child(chatId).child(STORED_MESSAGES)
                        .push()
                messageRef.setValue(message)
                emit(QueryState.Success(Unit))
            }.onFailure { e ->
                emit(QueryState.Error(e.message))
            }

        }


    override suspend fun editMessage(
        chatId: String,
        editedMessage: Message
    ): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatRoom(chatId: String): Flow<QueryState<ChatRoom>> = flow {
        kotlin.runCatching {
            val chatRoomRef =
                databaseReference.child(CHAT_COLLECTION).child(chatId).valueEvents.first()
            val chatRoom = chatRoomRef.value<ChatRoom>()
            emit(QueryState.Success(chatRoom))
        }.onFailure { e ->
            emit(QueryState.Error(e.message))
        }
    }

    override suspend fun getMessageById(
        messageId: String,
        chatId: String
    ): Flow<QueryState<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPinnedMessages(chatId: String): Flow<QueryState<List<Message>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStarredMessages(chatId: String): Flow<QueryState<List<Message>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserFromChatId(chatId: String): Flow<QueryState<List<String?>>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadInitialMessages(chatId: String): Flow<QueryState<List<Message>>> =
        flow {
            kotlin.runCatching {
                firebaseDataSource.getInitialMessages(chatId).collect {
                    when (it) {
                        is QueryState.Success -> {
                            emit(QueryState.Success(it.data))
                        }

                        is QueryState.Error -> {
                            emit(QueryState.Error(it.message))
                        }

                        else -> {

                        }
                    }
                }
            }.onFailure { e ->
                emit(QueryState.Error(e.message))
            }

        }

    override suspend fun loadMoreMessages(
        chatId: String,
        lastKey: String
    ): Flow<QueryState<List<Message>>> = flow{
        kotlin.runCatching {
            firebaseDataSource.getMoreMessages(chatId = chatId, lastKey = lastKey).collect {
                when (it) {
                    is Response.Success -> {
                        emit(QueryState.Success(it.data))
                    }

                    is Response.Error -> {
                        emit(QueryState.Error(it.message))
                    }

                    else -> {

                    }
                }
            }
        }.onFailure { e ->
            emit(QueryState.Error(e.message))
        }
    }

    override suspend fun markMessageAsRead(
        messageId: String,
        chatId: String
    ): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun observeGroupStatus(groupId: String): Flow<QueryState<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun observeUserStatusInGroup(
        groupId: String,
        userId: String
    ): Flow<QueryState<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun pinMessage(chatId: String, messageId: String): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun starMessage(chatId: String, messageId: String): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun setLastMessage(
        chatId: String,
        lastMessage: Message
    ): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun unPinMessage(chatId: String, messageId: String): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun unStarMessage(
        chatId: String,
        messageId: String
    ): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadAudio(audioFilePath: String): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadVideo(uri: Uri): Flow<QueryState<String>> {
        TODO("Not yet implemented")
    }


}