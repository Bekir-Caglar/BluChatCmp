package com.bekircaglar.bluchat.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import kotlinx.coroutines.delay

@Composable
fun ToastNotificationComponent(
    notification: NotificationData?,
    toastNotificationManager: ToastNotificationManager
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        var isVisible by remember { mutableStateOf(notification != null) }

        LaunchedEffect(notification) {
            if (notification != null) {
                isVisible = true
                delay(3000)
                isVisible = false
                toastNotificationManager.dismissNotification()
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        ) {
            notification?.let { notificationData ->
                ToastNotificationDialog(
                    notificationData = notificationData,
                    onDismiss = {
                        isVisible = false
                        toastNotificationManager.dismissNotification()
                    }
                )
            }
        }
    }
}
