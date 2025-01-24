package com.bekircaglar.bluchat.domain.usecase.profile

import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.repository.ProfileRepository
import com.bekircaglar.bluchat.utils.QueryState
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase (private val profileRepository: ProfileRepository) {
    suspend operator fun invoke() : Flow<QueryState<Users>> = profileRepository.getCurrentUser()
    suspend fun getUsersByListOfIds(userIds: List<String?>): Flow<QueryState<List<Users>>> = profileRepository.getUsersByListOfIds(userIds)
    suspend fun getUserById(userId: String): Flow<QueryState<Users>> = profileRepository.getUserById(userId)

}