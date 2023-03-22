package com.mock.data.database.entity

import org.jetbrains.exposed.sql.Table

object UserEntity: Table("User"){
    val id = integer("id").autoIncrement()
    val username = varchar("username", 64).uniqueIndex()
    val password = varchar("password", 64)
    override val primaryKey = PrimaryKey(id)
}