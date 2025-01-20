package com.bekircaglar.bluchat.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_account
import bluchatkmp.composeapp.generated.resources.ic_help
import bluchatkmp.composeapp.generated.resources.ic_logout
import bluchatkmp.composeapp.generated.resources.ic_privacy
import bluchatkmp.composeapp.generated.resources.ic_sun
import coil3.Uri
import com.bekircaglar.bluchat.domain.model.ProfileTabItem
import com.bekircaglar.bluchat.domain.model.ProfileUserUiState
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.CheckIsUserAlreadyExistUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.GetCurrentUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UpdateUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UploadImageUseCase
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.utils.UiState
import com.bekircaglar.bluchat.utils.data
import com.bekircaglar.bluchat.utils.error
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authUseCase: AuthUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val checkIsUserAlreadyExistUseCase: CheckIsUserAlreadyExistUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _updateUserState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val updateUserState = _updateUserState.asStateFlow()

    private val _profileUserUiState = MutableStateFlow(Users())
    val profileUserUiState = _profileUserUiState.asStateFlow()

    private val _menuItemList = MutableStateFlow<List<ProfileTabItem>>(emptyList())
    val menuItemList = _menuItemList.asStateFlow()

    private val _logOutState = MutableStateFlow<QueryState<Unit>>(QueryState.Idle)
    val logOutState = _logOutState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _uploadedImageUri = MutableStateFlow<Uri?>(null)
    val uploadedImageUri = _uploadedImageUri.asStateFlow()

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading = _isImageLoading.asStateFlow()

    private val _accountDialogState = MutableStateFlow(false)
    val accountDialogState = _accountDialogState.asStateFlow()

    private val _appearanceDialogState = MutableStateFlow(false)
    val appearanceDialogState = _appearanceDialogState.asStateFlow()


    fun onImageSelected(uri: Uri) {
        _selectedImageUri.value = uri
        uploadImage(uri)
    }

    private fun onImageUploaded(uri: Uri) {
        _uploadedImageUri.value = uri
    }


    init {
        fetchCurrentUser()

        _menuItemList.value = listOf(
            ProfileTabItem(
                title = "Account",
                icon = Res.drawable.ic_account,
                onClick = {
                    _accountDialogState.value = true
                }
            ),
            ProfileTabItem(
                title = "Appearance",
                icon = Res.drawable.ic_sun,
                onClick = {
                    _appearanceDialogState.value = true
                }
            ),
            ProfileTabItem(
                title = "Privacy",
                icon = Res.drawable.ic_privacy,
                onClick = { /*TODO*/ }
            ),
            ProfileTabItem(
                title = "Help",
                icon = Res.drawable.ic_help,
                onClick = { /*TODO*/ }
            ),
            ProfileTabItem(
                title = "Logout",
                icon = Res.drawable.ic_logout,
                onClick = {
                    signOut()
                }
            )
        )
    }

    fun clearUpdateUserState(){
        _updateUserState.value = QueryState.Idle
    }

    fun updateProfile(updatedUser: Users) = viewModelScope.launch {
        _uiState.update{ QueryState.Loading }
        checkIsUserAlreadyExistUseCase.checkPhoneNumber(updatedUser.phoneNumber.toString())
            .collect { emailExist ->
                when (emailExist) {
                    is QueryState.Success -> {
                        if (updatedUser == _profileUserUiState.value) {
                            _uiState.update { QueryState.Success(Unit) }
                        } else
                        if (emailExist.data) {
                            updateUserUseCase(updatedUser.copy(phoneNumber = _profileUserUiState.value.phoneNumber)).collect{
                                when(it){
                                    is QueryState.Success -> {
                                        _uiState.update { QueryState.Success(Unit) }
                                        if (updatedUser.phoneNumber == _profileUserUiState.value.phoneNumber)
                                            _updateUserState.update { QueryState.Success(Unit) }
                                        else
                                            _updateUserState.update { QueryState.Error("Your information has been updated successfully, except for the phone number. The phone number you entered is already in use by another account.") }
                                    }

                                    is QueryState.Error -> {
                                        _uiState.update { QueryState.Error(it.error) }

                                    }

                                    else -> {
                                        _uiState.update { QueryState.Idle }
                                    }
                                }
                            }
                        } else {
                            updateUserUseCase(updatedUser).collect{
                                when(it){
                                    is QueryState.Success -> {
                                        _uiState.update { QueryState.Success(Unit) }
                                        _updateUserState.update { QueryState.Success(Unit) }
                                    }

                                    is QueryState.Error -> {
                                        _uiState.update { QueryState.Error(it.error) }
                                    }

                                    else -> {
                                        _uiState.update { QueryState.Idle }
                                    }
                                }
                            }
                            _uiState.update { QueryState.Success(Unit) }
                        }
                    }

                    is QueryState.Error -> {
                        _uiState.value = QueryState.Error(emailExist.message)
                    }

                    else -> {
                        _uiState.value = QueryState.Idle
                    }
                }
            }


    }

    fun onAccountDialogDismiss() {
        _accountDialogState.value = false
    }

    fun onAppearanceDialogDismiss() {
        _appearanceDialogState.value = false
    }

    private fun uploadImage(uri: Uri) = viewModelScope.launch {
        _isImageLoading.value = true
        uploadImageUseCase(uri).collect {
            when (it) {
                is QueryState.Success -> {
                    onImageUploaded(it.data)
                    _isImageLoading.value = false
                }

                is QueryState.Error -> {
                    _uiState.update { QueryState.Error(it.error) }
                }

                else -> {
                    _uiState.update { QueryState.Idle }
                }
            }
        }
    }


    private fun fetchCurrentUser() = viewModelScope.launch {
        _uiState.value = QueryState.Loading
        getCurrentUserUseCase().collect { queryState ->
            when (queryState) {
                is QueryState.Success -> {
                    _profileUserUiState.update { queryState.data }
                    _uiState.update { QueryState.Success(Unit) }
                }

                is QueryState.Error -> {
                    _uiState.update { QueryState.Error(queryState.message) }
                }

                else -> {
                    _uiState.update { QueryState.Idle }
                }
            }
        }

    }


    private fun signOut() = viewModelScope.launch {
        _logOutState.value = QueryState.Loading
        authUseCase.signOut().collect { queryState ->
            when (queryState) {
                is QueryState.Success -> {
                    _logOutState.update { QueryState.Success(Unit) }
                }

                is QueryState.Error -> {
                    _logOutState.update { QueryState.Error(queryState.message) }
                }

                else -> {
                    _logOutState.update { QueryState.Idle }
                }
            }
        }
    }
}