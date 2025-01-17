package com.bekircaglar.bluchat.domain.usecase.auth

import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.repository.AuthRepository
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.Response
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

class AuthUseCase (private val authRepository: AuthRepository) {
    suspend fun signIn(email: String, password: String): Flow<QueryState<Unit>> = authRepository.signIn(email = email, password =  password)
    suspend fun signUp(email: String, password: String): Flow<QueryState<Unit>> = authRepository.signUp(email = email, password =password)
    suspend fun signOut(): Flow<QueryState<Unit>> = authRepository.signOut()
    suspend fun signInWithGoogle(idToken: String): Flow<QueryState<AuthResult>> = authRepository.signInWithGoogle(idToken = idToken)
    suspend fun crateUser(user:Users): Flow<QueryState<Unit>> = authRepository.createUser(users = user)
    suspend fun deleteUser(): Flow<QueryState<Unit>> = authRepository.deleteUser()
}
