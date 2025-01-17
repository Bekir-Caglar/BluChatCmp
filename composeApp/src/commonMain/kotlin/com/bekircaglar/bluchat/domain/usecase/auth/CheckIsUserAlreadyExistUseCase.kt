package com.bekircaglar.bluchat.domain.usecase.auth

import com.bekircaglar.bluchat.domain.repository.AuthRepository
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

class CheckIsUserAlreadyExistUseCase (private val authRepository: AuthRepository) {

    suspend fun checkEmail(email: String) : Flow<QueryState<Boolean>> = authRepository.checkIsUserAlreadyExist(email)
    suspend fun checkPhoneNumber(phoneNumber: String) : Flow<QueryState<Boolean>> = authRepository.checkPhoneNumber(phoneNumber)

}