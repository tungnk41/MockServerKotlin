package com.mock.dao

import com.mock.models.NoteEntity
import com.mock.models.UserEntity
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val driver = config.property("ktor.database.driver").getString()
        val prefixUrl = config.property("ktor.database.prefixUrl").getString()
        val host = config.property("ktor.database.host").getString()
        val port = config.property("ktor.database.port").getString()
        val url = prefixUrl+host+":"+port
        val username = config.property("ktor.database.user").getString()
        val password = config.property("ktor.database.password").getString()
        val database = config.property("ktor.database.database").getString()

        System.out.println(url)
        val connectionPool = createHikari(
            url = "$url/$database?user=$username&password=$password",
            driver = driver,
        )
        Database.connect(connectionPool)
        transaction {
            SchemaUtils.create(UserEntity)
            SchemaUtils.create(NoteEntity)
        }
    }

    private fun createHikari(url: String, driver: String) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = 15
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    suspend fun <T> query(
        block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}