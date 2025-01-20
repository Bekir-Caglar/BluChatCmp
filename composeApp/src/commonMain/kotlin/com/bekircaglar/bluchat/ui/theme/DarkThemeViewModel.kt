package com.bekircaglar.bluchat.ui.theme

import androidx.lifecycle.ViewModel
import com.bekircaglar.bluchat.domain.usecase.DarkThemeUseCase
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DarkThemeViewModel(private val darkThemeUseCase: DarkThemeUseCase) : ViewModel() {
    private val _darkTheme = MutableStateFlow<Boolean>(darkThemeUseCase.getDarkTheme())
    val darkTheme = _darkTheme.asStateFlow()

    fun toggleDarkTheme() {
        val currentState = _darkTheme.value
        darkThemeUseCase.toggleDarkTheme(currentState)
        _darkTheme.value = !currentState
    }
}