package com.bekircaglar.bluchat.domain.usecase.profile

import coil3.Uri
import com.bekircaglar.bluchat.domain.repository.ProfileRepository

class UploadImageUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(uri: Uri) = profileRepository.uploadImage(uri)
}