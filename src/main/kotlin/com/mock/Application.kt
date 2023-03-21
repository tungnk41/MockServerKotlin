package com.mock

import com.mock.repository.data.source.local.database.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.mock.plugins.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        DatabaseFactory.init()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
