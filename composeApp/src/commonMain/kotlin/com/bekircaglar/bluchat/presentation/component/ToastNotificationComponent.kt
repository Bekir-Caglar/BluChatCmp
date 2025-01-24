package com.bekircaglar.bluchat.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.utils.NotificationType
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import kotlinx.coroutines.delay
import kotlinx.datetime.format.Padding

@Composable
fun ToastNotificationComponent(
    notification: NotificationData?,
    topPadding: Dp,
    toastNotificationManager: ToastNotificationManager
) {

    // Disposible effcet ile çöz dispose durumunu

    Box(
        modifier = Modifier.fillMaxSize().padding(top = topPadding),
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
