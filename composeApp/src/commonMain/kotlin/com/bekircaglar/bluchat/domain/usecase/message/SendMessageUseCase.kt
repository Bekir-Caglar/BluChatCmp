package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.repository.MessageRepository

class SendMessageUseCase (private val messageRepository: MessageRepository) {

    suspend operator fun invoke(message: Message, chatId: String) = messageRepository.sendMessage(message = message, chatId = chatId)

}