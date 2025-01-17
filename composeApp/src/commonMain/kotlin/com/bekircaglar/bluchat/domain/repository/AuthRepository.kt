package com.bekircaglar.bluchat.domain.repository

import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.Response
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signIn(email: String, password: String) :Flow<QueryState<Unit>>

    suspend fun signUp(email: String, password: String) :Flow<QueryState<Unit>>

    suspend fun signOut() :Flow<QueryState<Unit>>

    suspend fun signInWithGoogle(idToken: String) :Flow<QueryState<AuthResult>>

    suspend fun checkIsUserAlreadyExist(email: String) :Flow<QueryState<Boolean>>

    suspend fun checkPhoneNumber(phoneNumber: String) :Flow<QueryState<Boolean>>

    suspend fun createUser(users: Users) :Flow<QueryState<Unit>>

    suspend fun deleteUser() :Flow<QueryState<Unit>>


}