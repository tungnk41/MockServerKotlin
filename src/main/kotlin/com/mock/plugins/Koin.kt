package com.mock.plugins

import com.mock.application.di.*
import com.mock.data.di.DaoModule
import com.mock.data.di.DatabaseModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        modules(
            AppModule,
            AuthModule,
            DaoModule,
            DatabaseModule,
            ControllerModule,
        )
    }
}