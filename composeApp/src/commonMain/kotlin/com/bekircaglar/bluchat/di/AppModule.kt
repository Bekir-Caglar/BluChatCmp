package com.bekircaglar.bluchat.di

import com.bekircaglar.bluchat.data.repository.AuthRepositoryImp
import com.bekircaglar.bluchat.data.repository.ProfileRepositoryImp
import com.bekircaglar.bluchat.data.repository.SettingsRepositoryImpl
import com.bekircaglar.bluchat.domain.repository.AuthRepository
import com.bekircaglar.bluchat.domain.repository.ProfileRepository
import com.bekircaglar.bluchat.domain.repository.SettingsRepository
import com.bekircaglar.bluchat.domain.usecase.DarkThemeUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.CheckIsUserAlreadyExistUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.GetCurrentUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UpdateUserUseCase
import com.bekircaglar.bluchat.domain.usecase.profile.UploadImageUseCase
import com.bekircaglar.bluchat.presentation.auth.signin.SignInViewModel
import com.bekircaglar.bluchat.presentation.auth.signup.SignUpViewModel
import com.bekircaglar.bluchat.presentation.chatlist.ChatListViewModel
import com.bekircaglar.bluchat.presentation.profile.ProfileViewModel
import com.bekircaglar.bluchat.ui.theme.DarkThemeViewModel
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
        factory { DarkThemeUseCase(get()) }
        viewModel { DarkThemeViewModel(get()) }
        factory { Settings() }
        factory { SettingsRepositoryImpl(get()) } bind SettingsRepository::class


        factoryOf(::AuthRepositoryImp) { bind<AuthRepository>() }
        viewModelOf(::SignInViewModel)
        factoryOf(::CheckIsUserAlreadyExistUseCase) { bind<CheckIsUserAlreadyExistUseCase>() }
        factoryOf(::UpdateUserUseCase) { bind<UpdateUserUseCase>() }
        factoryOf(::AuthUseCase) { bind<AuthUseCase>() }
        viewModelOf(::SignUpViewModel)
        viewModelOf(::ChatListViewModel)
        factoryOf(::ProfileRepositoryImp) { bind<ProfileRepository>() }
        factoryOf(::GetCurrentUserUseCase) { bind<GetCurrentUserUseCase>() }
        factoryOf(::UploadImageUseCase) { bind<UploadImageUseCase>() }
        viewModelOf(::ProfileViewModel)
    }
}