package com.bekircaglar.bluchat.domain.repository

import coil3.Uri
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getCurrentUser() : Flow<QueryState<Users>>

    suspend fun uploadImage(uri: Uri) : Flow<QueryState<Uri>>

    suspend fun updateUser(updatedUser: Users) : Flow<QueryState<Unit>>

}