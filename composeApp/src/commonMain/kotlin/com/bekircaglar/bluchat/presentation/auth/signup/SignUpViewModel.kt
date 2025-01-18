package com.bekircaglar.bluchat.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.bluchat.domain.model.SignUpUiState
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.CheckIsUserAlreadyExistUseCase
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.data
import com.bekircaglar.bluchat.utils.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authUseCase: AuthUseCase,
    private val checkIsUserAlreadyExistUseCase: CheckIsUserAlreadyExistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val uiState: StateFlow<QueryState<Unit>> = _uiState

    private val _signUpState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val signUpState: StateFlow<QueryState<Unit>> = _signUpState

    private val _signUpUIState = MutableStateFlow(SignUpUiState())
    val signUpUIState = _signUpUIState.asStateFlow()


    fun updateName(name: String) {
        _signUpUIState.value = _signUpUIState.value.copy(name = name)
    }

    fun updateSurname(surname: String) {
        _signUpUIState.value = _signUpUIState.value.copy(surname = surname)
    }

    fun updateEmail(email: String) {
        _signUpUIState.value = _signUpUIState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _signUpUIState.value = _signUpUIState.value.copy(password = password)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _signUpUIState.value = _signUpUIState.value.copy(phoneNumber = phoneNumber)
    }

    fun clearState() {
        _signUpUIState.value = SignUpUiState()
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$".toRegex()
        return passwordRegex.matches(password)
    }

    fun signUp() = viewModelScope.launch {
        _uiState.value = QueryState.Loading

        val email = signUpUIState.value.email

        checkIsUserAlreadyExistUseCase.checkEmail(email = email).collect { emailResult ->
            if (emailResult.isSuccess) {
                if (emailResult.data == true) {
                    _uiState.value = QueryState.Error("User already exists")
                } else {
                    checkPhoneNumberAndSignUp()
                }
            } else {
                _uiState.value = QueryState.Error(emailResult.toString())
            }
        }
    }

    private fun createUser() = viewModelScope.launch {

        val name = signUpUIState.value.name
        val surname = signUpUIState.value.surname
        val email = signUpUIState.value.email
        val phoneNumber = signUpUIState.value.phoneNumber

        val user = Users(
            name = name,
            surname = surname,
            email = email,
            phoneNumber = phoneNumber,
            profileImageUrl = "https://picsum.photos/200/300"
        )
        authUseCase.crateUser(user = user).collect { createUserResult ->
            if (createUserResult.isSuccess) {
                _uiState.value = createUserResult
                _signUpState.value = createUserResult
            } else {
                _uiState.value = createUserResult
            }
        }
    }

    private fun checkPhoneNumberAndSignUp() = viewModelScope.launch {

        val phoneNumber = signUpUIState.value.phoneNumber

        checkIsUserAlreadyExistUseCase.checkPhoneNumber(phoneNumber = phoneNumber).collect { phoneResult ->
            if (phoneResult.isSuccess) {
                if (phoneResult.data == true) {
                    _uiState.value = QueryState.Error("Phone number already exists")
                }
                if (phoneResult.data == false) {
                    performSignUp()
                }
            } else {
                _uiState.value = QueryState.Error(phoneResult.toString())
            }
        }
    }

    private fun performSignUp() = viewModelScope.launch {

        val email = signUpUIState.value.email
        val password = signUpUIState.value.password

        authUseCase.signUp(email = email, password =  password).collect { signUpResult ->
            if (signUpResult.isSuccess) {
                createUser()
            } else {
                _uiState.value = signUpResult
            }
        }
    }
}