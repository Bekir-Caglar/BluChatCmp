package com.bekircaglar.bluchat.domain.model

import org.jetbrains.compose.resources.DrawableResource

class ProfileTabItem(
    val icon: DrawableResource,
    val title: String,
    val onClick: () -> Unit
) {
    fun onClick() {
        onClick()
    }
}