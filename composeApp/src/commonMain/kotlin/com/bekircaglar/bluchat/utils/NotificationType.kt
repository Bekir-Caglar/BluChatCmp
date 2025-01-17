package com.bekircaglar.bluchat.utils

import androidx.compose.ui.graphics.Color
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_attention
import bluchatkmp.composeapp.generated.resources.ic_close_circle
import bluchatkmp.composeapp.generated.resources.ic_info_circle
import bluchatkmp.composeapp.generated.resources.ic_notification
import bluchatkmp.composeapp.generated.resources.ic_tick_circle
import com.bekircaglar.bluchat.ui.theme.ErrorBorder
import com.bekircaglar.bluchat.ui.theme.ErrorRed
import com.bekircaglar.bluchat.ui.theme.InfoBlue
import com.bekircaglar.bluchat.ui.theme.InfoBorder
import com.bekircaglar.bluchat.ui.theme.SuccessBorder
import com.bekircaglar.bluchat.ui.theme.SuccessGreen
import com.bekircaglar.bluchat.ui.theme.WarningBorder
import com.bekircaglar.bluchat.ui.theme.WarningOrange
import org.jetbrains.compose.resources.DrawableResource

sealed class NotificationType(val backgroundColor: Color,val borderColor: Color, val icon: DrawableResource) {
    data object Error : NotificationType(backgroundColor = ErrorRed,borderColor = ErrorBorder, icon = Res.drawable.ic_close_circle)
    data object Warning : NotificationType(backgroundColor = WarningOrange,borderColor = WarningBorder, icon = Res.drawable.ic_attention)
    data object Success : NotificationType(backgroundColor = SuccessGreen,borderColor = SuccessBorder, icon = Res.drawable.ic_tick_circle)
    data object Info : NotificationType(backgroundColor = InfoBlue,borderColor = InfoBorder, icon = Res.drawable.ic_info_circle)
}