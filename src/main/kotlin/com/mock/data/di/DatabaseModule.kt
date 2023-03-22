package com.mock.data.di

import com.mock.data.database.DatabaseFactory
import org.koin.dsl.module

val DatabaseModule = module {
    single { DatabaseFactory() }
}