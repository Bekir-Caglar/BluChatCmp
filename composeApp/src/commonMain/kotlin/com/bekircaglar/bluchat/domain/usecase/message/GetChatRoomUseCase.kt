package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.repository.MessageRepository
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

class GetChatRoomUseCase (private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: String): Flow<QueryState<ChatRoom>> = messageRepository.getChatRoom(chatId)
}