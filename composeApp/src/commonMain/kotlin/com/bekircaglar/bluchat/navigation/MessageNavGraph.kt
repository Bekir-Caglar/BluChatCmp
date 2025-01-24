package com.bekircaglar.bluchat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bekircaglar.bluchat.presentation.message.MessageScreen


fun NavGraphBuilder.MessageNavGraph(
    navController: NavController
) {
    navigation(startDestination = Screens.MessageScreen.route, route = Screens.MessageNav.route) {
        composable(
            Screens.MessageScreen.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            val chatId = it.arguments?.getString("chatId")
            MessageScreen(
                navController,
                chatId!!,
            )
        }


    }
}