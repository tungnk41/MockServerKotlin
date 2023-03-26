package com.mock.server.application.di

import com.mock.server.application.auth.PasswordEncryptor
import com.mock.server.application.auth.TokenManager
import org.koin.dsl.module

val AuthModule = module {
    single { TokenManager() }
    single { PasswordEncryptor() }
}