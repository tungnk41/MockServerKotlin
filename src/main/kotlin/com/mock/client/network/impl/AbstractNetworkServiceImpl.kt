package com.mock.client.network.impl

import com.mock.plugins.configureLogging
import com.mock.plugins.configureSerialization
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

abstract class AbstractNetworkServiceImpl {

    companion object {
        const val BASE_URL = "http://localhost:8082/api/v1"
    }

    open val client = HttpClient(CIO) {
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        engine {
            maxConnectionsCount = 1000
            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
                requestTimeout = 5000
            }
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        configureSerialization()
        configureLogging()
    }
}