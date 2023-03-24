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
    configureSecurity()
    configureSerialization()
    configureException()
    configureLogging()
    configureShutdown()

    configureSession()
    configureWebsocket()
    configureRouting()
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
        var database_url = ""
//        url = environment.config.property(Environment.DB_URL.value).getString()
        if (database_url.isNullOrEmpty()) {
            val database_prefixUrl = environment.config.property(Environment.DB_PREFIX_URL.value).getString()
            val database_host = environment.config.property(Environment.DB_HOST.value).getString()
            val database_port = environment.config.property(Environment.DB_PORT.value).getString()
            val database_username = environment.config.property(Environment.DB_USERNAME.value).getString()
            val database_password = environment.config.property(Environment.DB_PASSWORD.value).getString()
            val database_name = environment.config.property(Environment.DB_DATABASE_NAME.value).getString()
            database_url = "$database_prefixUrl$database_host:$database_port/$database_name?user=$database_username&password=$database_password"
        }
        config = hashMapOf(
            Environment.JWT_SECRET to environment.config.property(Environment.JWT_SECRET.value).getString(),
            Environment.JWT_EXPIRED to environment.config.property(Environment.JWT_EXPIRED.value).getString(),
            Environment.DB_DRIVER to environment.config.property(Environment.DB_DRIVER.value).getString(),
            Environment.DB_URL to database_url,

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