package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.repository.MessageRepository

class StarMessageUseCase (private val messageRepository: MessageRepository)  {
    suspend operator fun invoke(messageId: String, chatId: String) = messageRepository.starMessage(messageId, chatId)

}