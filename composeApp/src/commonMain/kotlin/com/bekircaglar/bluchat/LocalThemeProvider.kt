package com.bekircaglar.bluchat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import com.bekircaglar.bluchat.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

class LocalThemeViewModel : ViewModel() {

    val isDarkTheme = mutableStateOf(false)

    fun toggleTheme(isDark: Boolean) {
        isDarkTheme.value = isDark

    }
}

val LocalTheme = staticCompositionLocalOf<LocalThemeManager> {
    error("No LocalThemeManager provided")
}

@Composable
fun LocalThemeProvider(content: @Composable () -> Unit) {
    val viewModel: LocalThemeViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    val manager = remember {
        LocalThemeManager(viewModel, scope)
    }

    CompositionLocalProvider(value = LocalTheme provides manager) {
        AppTheme(darkTheme = viewModel.isDarkTheme.value) {
            content()
        }

    }
}

class LocalThemeManager(
    private val viewModel: LocalThemeViewModel,
    private val scope: CoroutineScope
) {
    val isDark: Boolean
        get() = viewModel.isDarkTheme.value

    fun toggle() {
        scope.launch {
            viewModel.toggleTheme(!isDark)
        }
    }

}