package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.repository.MessageRepository
import com.bekircaglar.bluchat.utils.Response
import kotlinx.coroutines.flow.Flow

class LoadMoreMessagesUseCase (private val repository: MessageRepository) {
    suspend operator fun invoke(chatId: String, lastKey: String) = repository.loadMoreMessages(chatId, lastKey)

}