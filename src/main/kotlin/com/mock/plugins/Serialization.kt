package com.mock.plugins

import io.ktor.client.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.configureSerialization() {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}
