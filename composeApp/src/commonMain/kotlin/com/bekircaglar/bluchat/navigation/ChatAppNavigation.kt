package com.bekircaglar.bluchat.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.compose.getKoin

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChatAppNavigation(
    navController: NavHostController,
    onThemeChange: () -> Unit,
) {
    val auth : FirebaseAuth = getKoin().get()
    println("user is " + auth.currentUser)

    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) Screens.HomeNav.route else Screens.AuthNav.route
    ) {
        AuthNavGraph(navController)
        MainNavGraph(navController, onThemeChange)
    }

}