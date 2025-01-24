package com.bekircaglar.bluchat.domain.usecase.chats

import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.repository.ChatRepository
import com.bekircaglar.bluchat.utils.Response
import kotlinx.coroutines.flow.Flow

class SearchPhoneNumberUseCase (private val chatsRepository: ChatRepository) {
    suspend operator fun invoke(query: String) = chatsRepository.searchContacts(query)


}