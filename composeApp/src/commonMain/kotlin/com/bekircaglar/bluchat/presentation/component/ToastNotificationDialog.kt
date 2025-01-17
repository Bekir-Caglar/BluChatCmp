package com.bekircaglar.bluchat.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.ui.theme.AppTheme
import com.bekircaglar.bluchat.ui.theme.ErrorBorder
import com.bekircaglar.bluchat.ui.theme.InfoBorder
import com.bekircaglar.bluchat.ui.theme.SuccessBorder
import com.bekircaglar.bluchat.ui.theme.WarningBorder
import com.bekircaglar.bluchat.utils.NotificationType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ToastNotificationDialog(
    notificationData: NotificationData,
    onDismiss: () -> Unit
) {

    val elementColors = when(notificationData.type){
        is NotificationType.Error -> ErrorBorder
        is NotificationType.Warning -> WarningBorder
        is NotificationType.Success -> SuccessBorder
        is NotificationType.Info -> InfoBorder
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(notificationData.type.backgroundColor)
            .border(1.dp, notificationData.type.borderColor, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(notificationData.type.icon),
                contentDescription = null,
                colorFilter = when (notificationData.type) {
                    is NotificationType.Error -> ColorFilter.tint(ErrorBorder)
                    is NotificationType.Warning -> ColorFilter.tint(WarningBorder)
                    is NotificationType.Success -> ColorFilter.tint(SuccessBorder)
                    is NotificationType.Info -> ColorFilter.tint(InfoBorder)
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = notificationData.message,
                style = MaterialTheme.typography.bodyMedium,
                color = elementColors,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray
                )
            }
        }
    }
}
