package com.mock.server.application.data.database

import com.mock.server.Environment
import com.mock.server.config
import com.mock.server.application.data.database.entity.NoteEntity
import com.mock.server.application.data.database.entity.UserEntity
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseFactory {

    fun init() {
        var database_url = config[Environment.DB_URL]!!
        var database_driver = config[Environment.DB_DRIVER]!!

        val connectionPool = createHikari(
            url = database_url,
            driver = database_driver,
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
}

suspend fun <T> query(
    block: () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }