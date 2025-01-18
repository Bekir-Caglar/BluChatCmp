package com.bekircaglar.bluchat.domain.model

data class ProfileUserUiState(
    val name : String = "",
    val surname : String = "",
    val email : String = "",
    val phoneNumber : String = "",
    val profileImageUrl : String = "",
)
