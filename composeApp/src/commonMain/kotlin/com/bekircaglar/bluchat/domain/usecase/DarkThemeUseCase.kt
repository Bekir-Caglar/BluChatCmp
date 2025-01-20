package com.bekircaglar.bluchat.domain.usecase

import com.bekircaglar.bluchat.domain.repository.SettingsRepository

class DarkThemeUseCase(private val repository: SettingsRepository) {
    fun getDarkTheme(): Boolean = repository.isDarkThemeEnabled()

    fun toggleDarkTheme(currentState: Boolean) {
        repository.setDarkThemeEnabled(!currentState)
    }
}