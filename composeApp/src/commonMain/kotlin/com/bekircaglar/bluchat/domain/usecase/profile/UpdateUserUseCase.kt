package com.bekircaglar.bluchat.domain.usecase.profile

import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.repository.ProfileRepository

class UpdateUserUseCase (private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(updatedUser:Users) = profileRepository.updateUser(updatedUser)
}