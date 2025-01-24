package com.bekircaglar.bluchat.domain.repository

import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun createChatRoom(user1:String,user2: String,chatRoomId:String,): Flow<QueryState<String>>

    suspend fun createGroupChatRoom(
        currentUserId: String,
        groupMembers: List<String>,
        chatId: String,
        groupName: String,
        groupImg: String
    ): Flow<QueryState<String>>

    suspend fun getUsersChatList(): Flow<QueryState<List<ChatRoom>>>

    suspend fun searchContacts(query: String): Flow<QueryState<List<Users>>>


}