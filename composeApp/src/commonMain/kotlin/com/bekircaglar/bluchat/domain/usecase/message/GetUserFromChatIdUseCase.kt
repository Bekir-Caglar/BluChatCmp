package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.data.repository.MessageRepositoryImp
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

class GetUserFromChatIdUseCase (private val messageRepositoryImp: MessageRepositoryImp) {
    suspend operator fun invoke(chatId: String) = messageRepositoryImp.getUserFromChatId(chatId)
}