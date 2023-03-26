package com.mock.plugins

import io.ktor.client.*
import io.ktor.client.plugins.logging.*

fun HttpClientConfig<*>.configureLogging() {
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
}