package com.mock

import com.mock.dao.DatabaseFactory
import io.ktor.server.netty.*
import com.mock.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureAuthentication(environment.config)
    configureSerialization()
    configureException()

    configureRouting(environment.config)
}