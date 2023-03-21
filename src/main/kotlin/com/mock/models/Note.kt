package com.mock.models

import com.mock.models.UserEntity.references
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Note(val id: Int = -1, val title: String = "", val content: String = "")

object NoteEntity: Table("Note"){
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val content = varchar("content", 128)
    val userId = integer("user_id").references(UserEntity.id)
    override val primaryKey = PrimaryKey(id)
}