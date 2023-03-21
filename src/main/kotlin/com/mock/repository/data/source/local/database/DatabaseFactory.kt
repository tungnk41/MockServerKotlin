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

        val databaseUrl = URI(System.getenv("DATABASE_URL"))
        val dbUserInfo : String? = databaseUrl.userInfo
        var username: String? = null
        var password: String? = null

        dbUserInfo?.let {
            username = dbUserInfo.split(":")[0]
            password = dbUserInfo.split(":")[1]
            username?.let {
                config.username = username
            }
            password?.let {
                config.password = password
            }
        }

        val jdbcUrl = "jdbc:postgresql://${databaseUrl.host}:${databaseUrl.port}${databaseUrl.path}?sslmode=require"
        config.jdbcUrl = jdbcUrl
        config.driverClassName = System.getenv("DRIVER_NAME")
        config.maximumPoolSize = 5
        config.isAutoCommit = false
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> query(
        block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}