package com.bekircaglar.bluchat.domain.model

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val surname: String = ""
) {
}