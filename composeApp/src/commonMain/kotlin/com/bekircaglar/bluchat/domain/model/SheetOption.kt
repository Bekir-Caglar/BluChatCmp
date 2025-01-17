package com.bekircaglar.bluchat.domain.model

import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_group_chat
import bluchatkmp.composeapp.generated.resources.ic_new_chat
import org.jetbrains.compose.resources.DrawableResource

data class SheetOption(val title: String, val icon: DrawableResource) {
    companion object {
        val NewChat = SheetOption("New Chat", Res.drawable.ic_new_chat )
        val CreateGroupChat = SheetOption("Create Group Chat", Res.drawable.ic_group_chat)
    }
}