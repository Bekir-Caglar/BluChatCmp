package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class LoadInitialMessagesUseCase (private val repository: MessageRepository) {
    suspend operator fun invoke(chatId: String) = repository.loadInitialMessages(chatId)

}