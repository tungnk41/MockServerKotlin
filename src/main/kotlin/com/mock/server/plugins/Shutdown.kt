package com.mock.server.plugins

import io.ktor.server.application.*
import io.ktor.server.engine.*

fun Application.configureShutdown() {
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = "/shutdown"
        exitCodeSupplier = { 0 }
    }
}
