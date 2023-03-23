package com.mock.data.database.entity

import org.jetbrains.exposed.sql.Table

object NoteEntity: Table("Note"){
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val content = varchar("content", 128)
    val userId = integer("user_id").references(UserEntity.id)
    override val primaryKey = PrimaryKey(id)
}

data class Note(val id: Int? = null, val title: String = "", val content: String = "")