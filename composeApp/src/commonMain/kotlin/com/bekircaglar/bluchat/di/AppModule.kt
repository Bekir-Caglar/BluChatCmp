package com.bekircaglar.bluchat.di

import com.bekircaglar.bluchat.LocalThemeViewModel
import com.bekircaglar.bluchat.data.repository.AuthRepositoryImp
import com.bekircaglar.bluchat.data.repository.ChatRepositoryImp
import com.bekircaglar.bluchat.data.repository.FirebaseDataSource
import com.bekircaglar.bluchat.data.repository.MessageRepositoryImp
import com.bekircaglar.bluchat.data.repository.ProfileRepositoryImp
import com.bekircaglar.bluchat.domain.repository.AuthRepository
import com.bekircaglar.bluchat.domain.repository.ChatRepository
import com.bekircaglar.bluchat.domain.repository.MessageRepository
import com.bekircaglar.bluchat.domain.repository.ProfileRepository
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.CheckIsUserAlreadyExistUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.CreateChatRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.CreateGroupChatRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.GetUserChatListUseCase
import com.bekircaglar.bluchat.domain.usecase.chats.SearchPhoneNumberUseCase
import com.bekircaglar.bluchat.domain.usecase.message.CreateMessageRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.message.DeleteMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.EditMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetChatRoomUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetMessageByIdUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetPinnedMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetStarredMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.GetUserFromChatIdUseCase
import com.bekircaglar.bluchat.domain.usecase.message.LoadInitialMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.LoadMoreMessagesUseCase
import com.bekircaglar.bluchat.domain.usecase.message.MarkMessageAsReadUseCase
import com.bekircaglar.bluchat.domain.usecase.message.ObserveGroupStatusUseCase
import com.bekircaglar.bluchat.domain.usecase.message.ObserveUserStatusInGroupUseCase
import com.bekircaglar.bluchat.domain.usecase.message.PinMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.SendMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.SetLastMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.StarMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UnPinMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UnStarMessageUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UploadAudioUseCase
import com.bekircaglar.bluchat.domain.usecase.message.UploadVideoUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.GetCurrentUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UpdateUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UploadImageUseCase
import com.bekircaglar.bluchat.presentation.auth.signin.SignInViewModel
import com.bekircaglar.bluchat.presentation.auth.signup.SignUpViewModel
import com.bekircaglar.bluchat.presentation.chatlist.ChatListViewModel
import com.bekircaglar.bluchat.presentation.message.MessageViewModel
import com.bekircaglar.bluchat.presentation.profile.ProfileViewModel
import com.russhwolf.settings.Settings
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single { Firebase.auth }
        single { Firebase.database.reference() }
        factory { Settings() }

        viewModelOf(::LocalThemeViewModel)
        factoryOf(::AuthRepositoryImp) { bind<AuthRepository>() }
        viewModelOf(::SignInViewModel)
        factoryOf(::CheckIsUserAlreadyExistUseCase) { bind<CheckIsUserAlreadyExistUseCase>() }
        factoryOf(::UpdateUserUseCase) { bind<UpdateUserUseCase>() }
        factoryOf(::AuthUseCase) { bind<AuthUseCase>() }
        viewModelOf(::SignUpViewModel)
        factoryOf(::ProfileRepositoryImp) { bind<ProfileRepository>() }
        factoryOf(::GetCurrentUserUseCase) { bind<GetCurrentUserUseCase>() }
        factoryOf(::UploadImageUseCase) { bind<UploadImageUseCase>() }
        viewModelOf(::ProfileViewModel)

        viewModelOf(::ChatListViewModel)

        factoryOf(::ChatRepositoryImp) { bind<ChatRepository>() }

        factoryOf(::FirebaseDataSource) { bind<FirebaseDataSource>() }
        factoryOf(::CreateChatRoomUseCase) { bind<CreateChatRoomUseCase>() }
        factoryOf(::CreateGroupChatRoomUseCase) { bind<CreateGroupChatRoomUseCase>() }
        factoryOf(::SearchPhoneNumberUseCase) { bind<SearchPhoneNumberUseCase>() }
        factoryOf(::GetUserChatListUseCase) { bind<GetUserChatListUseCase>() }


        viewModel { MessageViewModel(get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get()) }

        factoryOf(::MessageRepositoryImp) { bind<MessageRepository>() }

        factoryOf(::CreateMessageRoomUseCase) { bind<CreateMessageRoomUseCase>() }
        factoryOf(::DeleteMessageUseCase) { bind<DeleteMessageUseCase>() }
        factoryOf(::EditMessageUseCase) { bind<EditMessageUseCase>() }
        factoryOf(::GetChatRoomUseCase) { bind<GetChatRoomUseCase>() }
        factoryOf(::GetMessageByIdUseCase) { bind<GetMessageByIdUseCase>() }
        factoryOf(::GetPinnedMessagesUseCase) { bind<GetPinnedMessagesUseCase>() }
        factoryOf(::GetStarredMessagesUseCase) { bind<GetStarredMessagesUseCase>() }
        factoryOf(::GetUserFromChatIdUseCase) { bind<GetUserFromChatIdUseCase>() }
        factoryOf(::LoadInitialMessagesUseCase) { bind<LoadInitialMessagesUseCase>() }
        factoryOf(::LoadMoreMessagesUseCase) { bind<LoadMoreMessagesUseCase>() }
        factoryOf(::MarkMessageAsReadUseCase) { bind<MarkMessageAsReadUseCase>() }
        factoryOf(::ObserveGroupStatusUseCase) { bind<ObserveGroupStatusUseCase>() }
        factoryOf(::ObserveUserStatusInGroupUseCase) { bind<ObserveUserStatusInGroupUseCase>() }
        factoryOf(::PinMessageUseCase) { bind<PinMessageUseCase>() }
        factoryOf(::SendMessageUseCase) { bind<SendMessageUseCase>() }
        factoryOf(::SetLastMessageUseCase) { bind<SetLastMessageUseCase>() }
        factoryOf(::StarMessageUseCase) { bind<StarMessageUseCase>() }
        factoryOf(::UnPinMessageUseCase) { bind<UnPinMessageUseCase>() }
        factoryOf(::UnStarMessageUseCase) { bind<UnStarMessageUseCase>() }
        factoryOf(::UploadAudioUseCase) { bind<UploadAudioUseCase>() }
        factoryOf(::UploadVideoUseCase) { bind<UploadVideoUseCase>() }
    }
}