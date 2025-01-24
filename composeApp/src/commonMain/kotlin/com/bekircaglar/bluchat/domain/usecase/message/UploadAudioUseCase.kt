package com.bekircaglar.bluchat.domain.usecase.message

import com.bekircaglar.bluchat.domain.repository.MessageRepository

class UploadAudioUseCase (private val messageRepository: MessageRepository) {
    suspend operator fun invoke(audioFilePath: String) = messageRepository.uploadAudio(audioFilePath)
}