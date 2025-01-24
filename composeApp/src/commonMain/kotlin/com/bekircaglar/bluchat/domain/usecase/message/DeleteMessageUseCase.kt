package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.repository.MessageRepository

class DeleteMessageUseCase (private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: String, messageId: String) = messageRepository.deleteMessage(chatId, messageId)
}