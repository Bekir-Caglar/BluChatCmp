package com.bekircaglar.bluchat

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.bekircaglar.bluchat.di.AppModule
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.navigation.ChatAppNavigation
import com.bekircaglar.bluchat.presentation.component.ToastNotificationDialog
import com.bekircaglar.bluchat.ui.theme.AppTheme
import com.bekircaglar.bluchat.utils.NotificationType
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(AppModule().appModule)
        }
    ){
        AppTheme {
            val navController = rememberNavController()
            ChatAppNavigation(navController = navController,){
            }
        }
    }

}

