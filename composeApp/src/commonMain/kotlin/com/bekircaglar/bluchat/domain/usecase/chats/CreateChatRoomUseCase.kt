package com.bekircaglar.bluchat.domain.usecase.chats

import com.bekircaglar.bluchat.domain.repository.ChatRepository

class CreateChatRoomUseCase (private val chatsRepository: ChatRepository) {

    suspend operator fun invoke(user1:String,user2: String,chatRoomId:String,) = chatsRepository.createChatRoom(user1,user2,chatRoomId)

}