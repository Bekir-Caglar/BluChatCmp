package com.bekircaglar.bluchat.utils

import com.bekircaglar.bluchat.domain.model.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow

class ToastNotificationManager {

    private val _notificationFlow = MutableStateFlow<NotificationData?>(null)
    val notificationFlow: StateFlow<NotificationData?> = _notificationFlow

    fun showNotification(notificationData: NotificationData) {
        _notificationFlow.value = notificationData
    }


    fun dismissNotification() {
        _notificationFlow.value = null
    }


}