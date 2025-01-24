package com.bekircaglar.bluchat.domain.model

data class TopBarUser(
    val profileImageUrl: String? = "",
    val name: String? = "",
    val surname: String? = "",
    val chatName: String? = "",
    val isOnline: Boolean? = false
) {

}