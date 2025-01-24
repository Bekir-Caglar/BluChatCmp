package com.bekircaglar.bluchat.domain.usecase.chats

import com.bekircaglar.bluchat.domain.repository.ChatRepository

class CreateGroupChatRoomUseCase (private val chatsRepository: ChatRepository) {

    suspend operator fun invoke(
        currentUserId: String,
        groupMembers: List<String>,
        chatId: String,
        groupName: String,
        groupImg: String
    ) = chatsRepository.createGroupChatRoom(currentUserId, groupMembers, chatId, groupName, groupImg)
}