package com.mock.repository.data.source.local.database.entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object TodoEntity: Table() {
    val id : Column<Long> = long("id").autoIncrement().primaryKey()
    val user_id : Column<Long> = long("user_id").references(UserEntity.id)
    val title = varchar("title", 512)
    val content = varchar("content", 512)
    val done = bool("done")
}