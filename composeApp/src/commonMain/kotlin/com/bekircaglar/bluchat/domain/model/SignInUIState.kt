package com.bekircaglar.bluchat.domain.model

import com.mmk.kmpauth.google.GoogleUser

data class SignInUIState(
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    var googleUser: GoogleUser? = null
){

}