package com.bekircaglar.bluchat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUserUiState(
    val name : String = "",
    val surname : String = "",
    val email : String = "",
    val phoneNumber : String = "",
    val profileImageUrl : String = "",
)
