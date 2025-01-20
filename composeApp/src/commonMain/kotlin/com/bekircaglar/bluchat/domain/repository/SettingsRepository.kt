package com.bekircaglar.bluchat.domain.repository

interface SettingsRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
}