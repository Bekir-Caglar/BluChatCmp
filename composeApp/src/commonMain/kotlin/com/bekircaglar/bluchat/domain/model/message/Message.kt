package com.bekircaglar.bluchat.domain.model.message

import kotlinx.serialization.Serializable


enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    LOCATION,
    CONTACT,
    DOCUMENT,
    STICKER,
}

@Serializable
data class Message(
    var messageId: String? = "",
    val senderId: String? = "",
    val message: String? = "",
    val timestamp: Long? = 0L,
    val read: Boolean? = false,
    val messageType: String? = "",
    val edited: Boolean? = false,
    val pinned: Boolean? = false,
    val deleted : Boolean? = false,
    val starred: Boolean? = false,
    val replyTo: String? = "",
    val imageUrl: String? = "",
    val videoUrl: String? = "",
    val audioUrl: String? = "",
    val audioDuration: Int? = 0,
    val locationName: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val deletedAt : Long? = 0L,
    val updatedAt : Long? = 0L
) {
    val isRead: Boolean
        get() = read == true

    val isDeleted: Boolean
        get() = deleted == true

    val isEdited: Boolean
        get() = edited == true

    val isPinned: Boolean
        get() = pinned == true

    val isStarred: Boolean
        get() = starred == true

    val useMessage: String
        get() = message ?: ""

    val useImageUrl: String
        get() = imageUrl ?: ""

    val useVideoUrl: String
        get() = videoUrl ?: ""

    val useAudioUrl: String
        get() = audioUrl ?: ""

    val useAudioDuration: Int
        get() = audioDuration ?: 0

    val useLocationName: String
        get() = locationName ?: ""

    val useLatitude: Double
        get() = latitude ?: 0.0

    val useLongitude: Double
        get() = longitude ?: 0.0

    val useReplyTo: String
        get() = replyTo ?: ""

}







