package com.bekircaglar.bluchat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val email: String? = "",
    val uid: String? = "",
    val name: String? = "",
    val surname: String? = "",
    val phoneNumber: String? = "",
    val profileImageUrl: String? = "",
    val status: Boolean? = false,
    val lastSeen: Long? = 0L,
)
