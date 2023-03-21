package com.mock

import com.mock.repository.data.source.local.database.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.mock.plugins.*
import io.ktor.application.*

fun main() {
    embeddedServer(Netty) {
        DatabaseFactory.init()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
