package com.bekircaglar.bluchat.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bekircaglar.bluchat.presentation.auth.signin.SignInScreen
import com.bekircaglar.bluchat.presentation.auth.signup.SignUpScreen
import com.bekircaglar.bluchat.presentation.chatlist.ChatListScreen
import com.bekircaglar.bluchat.presentation.profile.ProfileScreen
import com.bekircaglar.bluchat.ui.theme.DarkThemeViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.MainNavGraph(
    navController: NavController,
    darkThemeViewModel: DarkThemeViewModel
) {
    navigation(startDestination = Screens.ChatListScreen.route, route = Screens.HomeNav.route) {
        composable(Screens.ChatListScreen.route) {
            ChatListScreen(navController)
        }
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController,darkThemeViewModel)
        }
    }


}