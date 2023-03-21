package com.mock.repository.data.source.local.database.entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserEntity : Table() {
    val id : Column<Long> = long("id").autoIncrement().primaryKey()
    val username = varchar("username", 128).uniqueIndex()
    val password = varchar("password", 64)
}