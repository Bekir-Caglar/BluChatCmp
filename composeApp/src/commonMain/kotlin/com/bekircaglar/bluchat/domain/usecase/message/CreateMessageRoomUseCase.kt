package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.domain.repository.MessageRepository
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

class CreateMessageRoomUseCase (private val messageRepository: MessageRepository) {

    suspend operator fun invoke(chatId: String) = messageRepository.createMessageRoom(chatId)
}