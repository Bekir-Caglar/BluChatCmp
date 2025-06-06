package com.bekircaglar.bluchat

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.bekircaglar.bluchat.di.AppModule
import com.bekircaglar.bluchat.navigation.ChatAppNavigation
import com.bekircaglar.bluchat.ui.theme.AppTheme
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
    ) {
        LocalThemeProvider{
            val navController = rememberNavController()
            ChatAppNavigation(navController = navController, )
        }
    }

}

