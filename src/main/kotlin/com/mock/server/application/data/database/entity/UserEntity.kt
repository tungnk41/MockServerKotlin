package com.mock.server.application.data.database.entity

import org.jetbrains.exposed.sql.Table

object UserEntity: Table("User"){
    val id = integer("id").autoIncrement()
    val username = varchar("username", 64).uniqueIndex()
    val password = varchar("password", 64)
    override val primaryKey = PrimaryKey(id)
}

data class User(val id: Int, val username: String = "", val password: String = "")