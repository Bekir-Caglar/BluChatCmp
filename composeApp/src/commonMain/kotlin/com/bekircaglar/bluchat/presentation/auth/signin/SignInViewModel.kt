package com.bekircaglar.bluchat.presentation.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.domain.model.SignInUIState
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.CheckIsUserAlreadyExistUseCase
import com.bekircaglar.bluchat.utils.NotificationType
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import com.bekircaglar.bluchat.utils.data
import com.bekircaglar.bluchat.utils.isSuccess
import com.bekircaglar.bluchat.utils.name
import com.bekircaglar.bluchat.utils.surname
import com.mmk.kmpauth.google.GoogleUser
import dev.gitlive.firebase.auth.AuthResult
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authUseCase: AuthUseCase,
    private val checkIsUserAlreadyExistUseCase: CheckIsUserAlreadyExistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _authResultState = MutableStateFlow<QueryState<AuthResult>>(QueryState.Idle)
    val authResultState = _authResultState.asStateFlow()

    private val _signInState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val signInState = _signInState.asStateFlow()

    private val _signInUIState = MutableStateFlow(SignInUIState())
    val signInUIState = _signInUIState.asStateFlow()

    private val _showPhoneNumberDialog = MutableStateFlow(false)
    val showPhoneNumberDialog = _showPhoneNumberDialog.asStateFlow()

    private val _toastNotificationManager = MutableStateFlow<ToastNotificationManager?>(null)
    val toastNotificationManager = _toastNotificationManager.asStateFlow()


    init {
        _toastNotificationManager.value = ToastNotificationManager()
    }

    fun clearState() {
        _signInState.value = QueryState.Idle
    }

    fun dismissPhoneNumberDialog() {
        _showPhoneNumberDialog.value = false
    }

    fun updateSignInEmail(email: String) {
        _signInUIState.value = _signInUIState.value.copy(email = email)
    }

    fun updateSignInPassword(password: String) {
        _signInUIState.value = _signInUIState.value.copy(password = password)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _signInUIState.value = _signInUIState.value.copy(phoneNumber = phoneNumber)
    }

    fun setGoogleUser(googleUser: GoogleUser) {
        _signInUIState.value.googleUser = googleUser
    }


    fun signIn() = viewModelScope.launch {
        val email = _signInUIState.value.email
        val password = _signInUIState.value.password

        authUseCase.signIn(email = email, password = password).collect { signInResult ->
            _signInState.update { signInResult }
        }
    }


    fun signInWithGoogle() = viewModelScope.launch {
        _uiState.value = QueryState.Loading
        val googleUser = _signInUIState.value.googleUser

        googleUser?.let { user ->
            user.email?.let { email ->
                checkIsUserAlreadyExistUseCase.checkEmail(email = email).collect { emailResult ->
                    if (emailResult.isSuccess) {
                        user.idToken.let { idToken ->
                            authUseCase.signInWithGoogle(idToken = idToken)
                                .collect { authResult ->
                                    _authResultState.value = authResult
                                }
                        }
                    }
                    if (emailResult.data == false) {
                        _showPhoneNumberDialog.value = true
                    } else if (emailResult.data == true) {
                        _signInState.value = QueryState.Success(Unit)
                        _uiState.value = QueryState.Success(Unit)
                    }
                }
            }
        }
    }


    fun createGoogleUser() = viewModelScope.launch {
        val authResult = _authResultState.value.data
        val phoneNumber = _signInUIState.value.phoneNumber

        val user = authResult?.let { user ->
            Users(
                name = user.name(),
                surname = user.surname(),
                email = user.user?.email,
                phoneNumber = user.user?.phoneNumber ?: phoneNumber,
                profileImageUrl = user.user?.photoURL,
            )
        }

        checkIsUserAlreadyExistUseCase.checkPhoneNumber(phoneNumber = phoneNumber)
            .collect { phoneNumberResult ->
                if (phoneNumberResult.isSuccess && phoneNumberResult.data == false) {
                    if (user != null) {
                        authUseCase.crateUser(user = user).collect { databaseResult ->
                            if (databaseResult.isSuccess) {
                                _signInState.update { databaseResult }
                            }
                        }
                    }
                }
                if (phoneNumberResult.data == true) {
                    _uiState.update { QueryState.Error("Phone number already exists") }
                    _signInState.update { QueryState.Error("Phone number already exists") }
                    deleteCurrentUser()
                }
            }
    }

    fun deleteCurrentUser() = viewModelScope.launch {
        authUseCase.deleteUser().collect { result ->
            _uiState.update { result }
        }
    }

    fun signOut() = viewModelScope.launch {
        authUseCase.signOut().collect { result ->
            _uiState.update { result }
        }
    }

}