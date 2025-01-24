package com.bekircaglar.bluchat.domain.usecase.chats

import com.bekircaglar.bluchat.domain.model.ChatRoom
import com.bekircaglar.bluchat.domain.repository.ChatRepository
import com.bekircaglar.bluchat.utils.Response
import kotlinx.coroutines.flow.Flow

class GetUserChatListUseCase (private val chatsRepository: ChatRepository) {

    suspend operator fun invoke() = chatsRepository.getUsersChatList()

}