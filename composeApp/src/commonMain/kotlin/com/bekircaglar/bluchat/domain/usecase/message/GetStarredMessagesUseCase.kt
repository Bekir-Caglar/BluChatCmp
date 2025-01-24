package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.repository.MessageRepository

class GetStarredMessagesUseCase (private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: String) = messageRepository.getStarredMessages(chatId)

}