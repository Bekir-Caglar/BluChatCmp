package com.bekircaglar.bluchat.domain.repository

import coil3.Uri
import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun createMessageRoom(chatId: String): Flow<QueryState<Unit>>
    suspend fun deleteMessage(chatId: String, messageId: String): Flow<QueryState<String>>
    suspend fun sendMessage(chatId: String, message: Message): Flow<QueryState<Unit>>
    suspend fun editMessage(chatId: String, editedMessage: Message): Flow<QueryState<String>>
    suspend fun getChatRoom(chatId: String): Flow<QueryState<ChatRoom>>
    suspend fun getMessageById(messageId: String, chatId: String): Flow<QueryState<Message>>
    suspend fun getPinnedMessages(chatId: String): Flow<QueryState<List<Message>>>
    suspend fun getStarredMessages(chatId: String): Flow<QueryState<List<Message>>>
    suspend fun getUserFromChatId(chatId: String): Flow<QueryState<List<String?>>>
    suspend fun loadInitialMessages(chatId: String): Flow<QueryState<List<Message>>>
    suspend fun loadMoreMessages(chatId: String, lastKey: String): Flow<QueryState<List<Message>>>
    suspend fun markMessageAsRead(messageId: String, chatId: String): Flow<QueryState<String>>
    suspend fun observeGroupStatus(groupId: String): Flow<QueryState<Boolean>>
    suspend fun observeUserStatusInGroup(groupId: String, userId: String): Flow<QueryState<Boolean>>
    suspend fun pinMessage(chatId: String, messageId: String): Flow<QueryState<String>>
    suspend fun starMessage(chatId: String, messageId: String): Flow<QueryState<String>>
    suspend fun setLastMessage(chatId: String, lastMessage: Message): Flow<QueryState<String>>
    suspend fun unPinMessage(chatId: String, messageId: String): Flow<QueryState<String>>
    suspend fun unStarMessage(chatId: String, messageId: String): Flow<QueryState<String>>
    suspend fun uploadAudio(audioFilePath: String): Flow<QueryState<String>>
    suspend fun uploadVideo(uri: Uri): Flow<QueryState<String>>

}