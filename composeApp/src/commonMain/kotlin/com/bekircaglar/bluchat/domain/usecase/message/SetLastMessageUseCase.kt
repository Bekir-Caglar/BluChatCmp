package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.domain.repository.MessageRepository

class SetLastMessageUseCase (private val messageRepository: MessageRepository) {

    suspend operator fun invoke(chatId: String, lastMessage: Message) = messageRepository.setLastMessage(chatId, lastMessage)

}