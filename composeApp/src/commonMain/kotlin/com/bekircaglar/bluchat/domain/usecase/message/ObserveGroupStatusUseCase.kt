package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObserveGroupStatusUseCase (private val messageRepository: MessageRepository) {

    suspend operator fun invoke(groupId: String) = messageRepository.observeGroupStatus(groupId)

}