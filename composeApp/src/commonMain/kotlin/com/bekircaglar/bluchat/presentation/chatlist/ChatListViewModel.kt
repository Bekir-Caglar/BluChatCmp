package com.bekircaglar.bluchat.presentation.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatListViewModel(private val authUseCase: AuthUseCase):ViewModel()  {
    private val _uiState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val uiState = _uiState


    fun setLoadState(){
        _uiState.value = QueryState.Loading
    }

    fun signOut() = viewModelScope.launch{
        authUseCase.signOut().collect{
        }
    }
}