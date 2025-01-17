package com.bekircaglar.bluchat.di

import com.bekircaglar.bluchat.data.repository.AuthRepositoryImp
import com.bekircaglar.bluchat.domain.repository.AuthRepository
import com.bekircaglar.bluchat.domain.usecase.auth.AuthUseCase
import com.bekircaglar.bluchat.domain.usecase.auth.CheckIsUserAlreadyExistUseCase
import com.bekircaglar.bluchat.presentation.auth.signin.SignInViewModel
import com.bekircaglar.bluchat.presentation.auth.signup.SignUpViewModel
import com.bekircaglar.bluchat.presentation.chatlist.ChatListViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single { Firebase.auth }
        single { Firebase.database.reference() }

        factoryOf(::AuthRepositoryImp) { bind<AuthRepository>() }
        viewModelOf(::SignInViewModel)
        factoryOf(::CheckIsUserAlreadyExistUseCase) { bind<CheckIsUserAlreadyExistUseCase>() }
        factoryOf(::AuthUseCase) { bind<AuthUseCase>() }
        viewModelOf(::SignUpViewModel)
        viewModelOf(::ChatListViewModel)
    }
}