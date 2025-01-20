package com.bekircaglar.bluchat

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.bekircaglar.bluchat.di.AppModule
import com.bekircaglar.bluchat.navigation.ChatAppNavigation
import com.bekircaglar.bluchat.ui.theme.AppTheme
import com.bekircaglar.bluchat.ui.theme.DarkThemeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(AppModule().appModule)
        }
    ){
        val viewModel : DarkThemeViewModel = koinViewModel()
        val isDarkTheme = viewModel.darkTheme.collectAsStateWithLifecycle()

        AppTheme(darkTheme = isDarkTheme.value) {
            val navController = rememberNavController()
            ChatAppNavigation(navController = navController, darkThemeViewModel = viewModel)
        }
    }

}

