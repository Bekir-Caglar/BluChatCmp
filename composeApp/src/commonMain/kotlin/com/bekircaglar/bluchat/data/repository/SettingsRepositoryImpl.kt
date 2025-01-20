package com.bekircaglar.bluchat.data.repository

import com.bekircaglar.bluchat.domain.repository.SettingsRepository
import com.russhwolf.settings.Settings

class SettingsRepositoryImpl(private val settings: Settings) : SettingsRepository {
    companion object {
        private const val DARK_THEME_KEY = "dark_theme"
    }


    override fun isDarkThemeEnabled(): Boolean =
        settings.getBoolean(DARK_THEME_KEY, false)

    override fun setDarkThemeEnabled(enabled: Boolean) {
        settings.putBoolean(DARK_THEME_KEY, enabled)
    }
}