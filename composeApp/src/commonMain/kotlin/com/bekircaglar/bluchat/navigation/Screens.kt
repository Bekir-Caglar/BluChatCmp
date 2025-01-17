package com.bekircaglar.bluchat.navigation

import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_contacts
import bluchatkmp.composeapp.generated.resources.ic_filled_chat
import bluchatkmp.composeapp.generated.resources.ic_filled_contacts
import bluchatkmp.composeapp.generated.resources.ic_filled_person
import bluchatkmp.composeapp.generated.resources.ic_outlined_chat
import bluchatkmp.composeapp.generated.resources.ic_outlined_profile
import org.jetbrains.compose.resources.DrawableResource


sealed class Screens(
    val route:String,
    val selectedIcon: DrawableResource? = null,
    val unSelectedIcon: DrawableResource? = null, val badgeCount:Int = 0, val label:String? = null){
    data object SingInScreen:Screens("sign_in_screen",)
    data object SingUpScreen:Screens("sign_up_screen",)
    data object ChatListScreen:Screens("chat_screen", selectedIcon = Res.drawable.ic_filled_chat,
        unSelectedIcon = Res.drawable.ic_outlined_chat, label = "Chats")
    data object ProfileScreen:Screens("profile_screen", selectedIcon = Res.drawable.ic_filled_person,
        unSelectedIcon = Res.drawable.ic_outlined_profile, label = "Profile")
    data object MessageScreen:Screens("message_screen/{chatId}"){
        fun createRoute(chatId:String):String = "message_screen/$chatId"
    }
    data object ChatInfoScreen:Screens("chat_info_screen/{infoChatId}"){
        fun createRoute(chatId:String):String = "chat_info_screen/$chatId"
    }
    data object ImageScreen:Screens("image_screen/{imageUrl}"){
        fun createRoute(imageUrl:String):String = "image_screen/$imageUrl"
    }
    data object CameraScreen:Screens("camera_screen/{chatId}"){
        fun createRoute(chatId:String):String = "camera_screen/$chatId"
    }
    data object SendTakenPhotoScreen:Screens("send_taken_photo_screen/{imageUrl}/{chatId}"){
        fun createRoute(imageUrl:String,chatId:String):String = "send_taken_photo_screen/$imageUrl/$chatId"
    }
    data object StarredMessagesScreen:Screens("starred_messages_screen/{chatId}"){
        fun createRoute(chatId:String):String = "starred_messages_screen/$chatId"
    }
    data object ContactScreen:Screens("contact_screen", selectedIcon = Res.drawable.ic_filled_contacts, unSelectedIcon = Res.drawable.ic_contacts, label = "Contacts")


    data object AuthNav : Screens("AUTH_NAV_GRAPH")

    data object HomeNav : Screens("HOME_NAV_GRAPH")
}