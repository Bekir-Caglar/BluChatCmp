package com.bekircaglar.bluchat.domain.model

import com.bekircaglar.bluchat.domain.model.message.Message

data class Chats(
    val chatRoomId: String = "",
    val name : String = "",
    val surname : String = "",
    val chatName : String = "",
    val chatType : String = "",
    val chatImage : String = "",
    val lastMessage: Message? = null,
    val isOnline: Boolean = false,
) {
}