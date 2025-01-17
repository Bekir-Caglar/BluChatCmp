package com.bekircaglar.bluchat.domain.model

import com.bekircaglar.bluchat.utils.NotificationType

data class NotificationData(
    val message: String,
    val type: NotificationType,
)