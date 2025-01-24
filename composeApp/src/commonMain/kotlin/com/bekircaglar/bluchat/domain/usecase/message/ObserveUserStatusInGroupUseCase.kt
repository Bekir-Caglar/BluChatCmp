package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserStatusInGroupUseCase (private val messageRepository: MessageRepository) {

   suspend operator fun invoke(groupId: String, userId: String) = messageRepository.observeUserStatusInGroup(groupId, userId)

}