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
){
    fun mergeWith(existingUser: Users): Users {
        return Users(
            email = if (email.isNullOrBlank()) existingUser.email else email,
            uid = if (uid.isNullOrBlank()) existingUser.uid else uid,
            name = if (name.isNullOrBlank()) existingUser.name else name,
            surname = if (surname.isNullOrBlank()) existingUser.surname else surname,
            phoneNumber = if (phoneNumber.isNullOrBlank()) existingUser.phoneNumber else phoneNumber,
            profileImageUrl = if (profileImageUrl.isNullOrBlank()) existingUser.profileImageUrl else profileImageUrl,
            status = status ?: existingUser.status,
            lastSeen = if (lastSeen == 0L) existingUser.lastSeen else lastSeen
        )
    }

    val fullName: String
        get() = "$name $surname"

    val isOnline: Boolean
        get() = status == true

    val isOffline: Boolean
        get() = status == false

    val hasName: Boolean
        get() = !name.isNullOrBlank()

    val hasSurname: Boolean
        get() = !surname.isNullOrBlank()

    val hasEmail: Boolean
        get() = !email.isNullOrBlank()

    val hasPhoneNumber: Boolean
        get() = !phoneNumber.isNullOrBlank()

    val hasProfileImageUrl: Boolean
        get() = !profileImageUrl.isNullOrBlank()

    val hasStatus: Boolean
        get() = status != null
}
