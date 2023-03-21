package com.mock.repository.data.source.local.database

import com.mock.repository.data.source.local.database.entities.TodoEntity
import com.mock.repository.data.source.local.database.entities.UserEntity
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(UserEntity)
            SchemaUtils.create(TodoEntity)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()

        val dbUri = URI(System.getenv("DATABASE_URL") ?: "postgresql://localhost:5432/mockserver")
        val dbUserInfo : String? = dbUri.userInfo
        var username: String? = null
        var password: String? = null

        if(dbUserInfo != null) {
            username = dbUserInfo.split(":")[0]
            password = dbUserInfo.split(":")[1]
        }else {
            username = System.getenv("DATABASE_USERNAME")
            password = System.getenv("DATABASE_PASSWORD")
        }
        username?.let {
            config.username = username
        }

        password?.let {
            config.password = password
        }
        val dbUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}?sslmode=require"
        config.jdbcUrl = dbUrl
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.maximumPoolSize = 5
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> query(
        block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}