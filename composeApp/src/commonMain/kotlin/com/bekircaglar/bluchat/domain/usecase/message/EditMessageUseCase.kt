package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.repository.MessageRepository

class EditMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: String, message: Message) =
        messageRepository.editMessage(chatId = chatId, editedMessage = message)
}