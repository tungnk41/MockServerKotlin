package com.mock.server.application.data.di

import com.mock.server.application.data.database.DatabaseFactory
import org.koin.dsl.module

val DatabaseModule = module {
    single { DatabaseFactory() }
}