package com.bekircaglar.bluchat.domain.model

import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.utils.GROUP
import com.bekircaglar.bluchat.utils.PRIVATE
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoom(
    val users: List<String>? = emptyList(),
    val chatId: String? = "",
    val chatName: String? = "",
    val chatImage: String? = "",
    val chatType: String? = "",
    val chatAdminId: String? = "",
    val chatLastMessage: Message? = null,
    val chatCreatedAt: Long = 0L,
    val chatUpdatedAt: Long = 0L
) {

    fun usersList(): List<String> {
        return users ?: emptyList()
    }

    fun useChatId(): String {
        return chatId ?: ""
    }

    fun useChatName(): String {
        return chatName ?: ""
    }

    fun useChatImage(): String {
        return chatImage ?: ""
    }

    fun useChatType(): String {
        return chatType ?: ""
    }

    fun useChatAdminId(): String {
        return chatAdminId ?: ""
    }

    fun useChatCreatedAt(): Long {
        return chatCreatedAt
    }

    fun useChatUpdatedAt(): Long {
        return chatUpdatedAt
    }

    fun useChatLastMessage(): Message? {
        return chatLastMessage
    }

    fun isGroupChat(): Boolean {
        return chatType == GROUP
    }

    fun isPrivateChat(): Boolean {
        return chatType == PRIVATE
    }

    fun isChatAdmin(userId: String): Boolean {
        return chatAdminId == userId
    }





}
