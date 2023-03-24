package com.mock

import com.mock.data.database.DatabaseFactory
import com.mock.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
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
    configureLogging()
    configureShutdown()
    databaseFactory.init()
}

enum class Environment(val value: String) {
    JWT_SECRET("jwt.secret"),
    JWT_EXPIRED("jwt.expired"),
    DB_DRIVER("ktor.database.driver"),
    DB_URL("ktor.database.url"),
    DB_PREFIX_URL("ktor.database.prefixUrl"),
    DB_HOST("ktor.database.host"),
    DB_PORT("ktor.database.port"),
    DB_USERNAME("ktor.database.user"),
    DB_PASSWORD("ktor.database.password"),
    DB_DATABASE_NAME("ktor.database.database")
}
var config = hashMapOf<Environment,String>()
fun Application.parseEnvironment() {
    try {
        var url = ""
//        url = environment.config.property(Environment.DB_URL.value).getString()
        if (url.isNullOrEmpty()) {
            val prefixUrl = environment.config.property(Environment.DB_PREFIX_URL.value).getString()
            val host = environment.config.property(Environment.DB_HOST.value).getString()
            val port = environment.config.property(Environment.DB_PORT.value).getString()
            val username = environment.config.property(Environment.DB_USERNAME.value).getString()
            val password = environment.config.property(Environment.DB_PASSWORD.value).getString()
            val database = environment.config.property(Environment.DB_DATABASE_NAME.value).getString()
            url = "$prefixUrl$host:$port/$database?user=$username&password=$password"
        }
        config = hashMapOf(
            Environment.JWT_SECRET to environment.config.property(Environment.JWT_SECRET.value).getString(),
            Environment.JWT_EXPIRED to environment.config.property(Environment.JWT_EXPIRED.value).getString(),
            Environment.DB_DRIVER to environment.config.property(Environment.DB_DRIVER.value).getString(),
            Environment.DB_URL to url,

        )
    } catch (e: Exception) {
        e.printStackTrace()
        config = hashMapOf(
            Environment.JWT_SECRET to "Secret@123",
            Environment.JWT_EXPIRED to "600000", // 10 Minutes
            Environment.DB_DRIVER to "org.h2.Driver",
            Environment.DB_URL to "jdbc:h2:file:./build/database",
        )
        println("#### FAIL PARSE ENVIRONMENT CONFIG ->> Switch to Local Config")
    }
    finally {
        println("#### DATABASE : " + config[Environment.DB_URL])
    }
}