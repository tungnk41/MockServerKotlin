package com.mock.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(val id: Int = -1, val username: String = "", val password: String = "")

object UserEntity: Table("User"){
    val id = integer("id").autoIncrement()
    val username = varchar("username", 64).uniqueIndex()
    val password = varchar("password", 64)
    override val primaryKey = PrimaryKey(id)
}