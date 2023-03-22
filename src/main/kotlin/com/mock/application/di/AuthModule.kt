package com.mock.application.di

import com.mock.application.auth.*
import org.koin.dsl.module

val AuthModule = module {
    single { TokenManager() }
    single { PasswordEncryptor() }
}