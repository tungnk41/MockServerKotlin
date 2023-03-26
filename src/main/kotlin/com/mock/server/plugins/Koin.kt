package com.mock.server.plugins

import com.mock.client.di.clientNetworkModule
import com.mock.server.application.data.di.DaoModule
import com.mock.server.application.data.di.DatabaseModule
import com.mock.server.application.di.AppModule
import com.mock.server.application.di.AuthModule
import com.mock.server.application.di.ControllerModule
import com.mock.server.application.di.WebSocketModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            AppModule,
            AuthModule,
            DaoModule,
            DatabaseModule,
            ControllerModule,
            WebSocketModule,
            /* Client */
            clientNetworkModule,
        )
    }
}