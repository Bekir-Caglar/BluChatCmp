package com.bekircaglar.bluchat.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bekircaglar.bluchat.ui.theme.DarkThemeViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.compose.getKoin

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChatAppNavigation(
    navController: NavHostController,
    darkThemeViewModel: DarkThemeViewModel
) {
    val auth : FirebaseAuth = getKoin().get()


    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) Screens.HomeNav.route else Screens.AuthNav.route
    ) {
        AuthNavGraph(navController = navController)
        MainNavGraph(navController = navController, darkThemeViewModel = darkThemeViewModel ,)
    }

}