package com.mock

import com.mock.plugins.*
import com.mock.data.database.DatabaseFactory
import io.ktor.server.netty.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val databaseFactory by inject<DatabaseFactory>()
    parseEnvironment()

    configureKoin()
    configureAuthentication()
    configureSerialization()
    configureException()
    configureRouting()
    databaseFactory.init()
}

enum class Environment(val value: String) {
    JWT_SECRET("jwt.secret"),
    JWT_EXPIRED("jwt.expired"),
    DB_DRIVER("ktor.database.driver"),
    DB_PREFIX_URL("ktor.database.prefixUrl"),
    DB_HOST("ktor.database.host"),
    DB_PORT("ktor.database.port"),
    DB_USER("ktor.database.user"),
    DB_PASSWORD("ktor.database.password"),
    DB_DATABASE("ktor.database.database")
}
var config = hashMapOf<Environment,String>()
fun Application.parseEnvironment() {
    config  = hashMapOf(
        Environment.JWT_SECRET to environment.config.property(Environment.JWT_SECRET.value).getString(),
        Environment.JWT_EXPIRED to environment.config.property(Environment.JWT_EXPIRED.value).getString(),
        Environment.DB_DRIVER to environment.config.property(Environment.DB_DRIVER.value).getString(),
        Environment.DB_PREFIX_URL to environment.config.property(Environment.DB_PREFIX_URL.value).getString(),
        Environment.DB_HOST to environment.config.property(Environment.DB_HOST.value).getString(),
        Environment.DB_PORT to environment.config.property(Environment.DB_PORT.value).getString(),
        Environment.DB_USER to environment.config.property(Environment.DB_USER.value).getString(),
        Environment.DB_PASSWORD to environment.config.property(Environment.DB_PASSWORD.value).getString(),
        Environment.DB_DATABASE to environment.config.property(Environment.DB_DATABASE.value).getString(),
    )
}