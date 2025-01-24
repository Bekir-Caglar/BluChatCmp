package com.bekircaglar.bluchat.domain.usecase.message

import coil3.Uri
import com.bekircaglar.bluchat.domain.repository.MessageRepository

class UploadVideoUseCase (private val messageRepository: MessageRepository) {

    suspend operator fun invoke(uri: Uri) = messageRepository.uploadVideo(uri)

}