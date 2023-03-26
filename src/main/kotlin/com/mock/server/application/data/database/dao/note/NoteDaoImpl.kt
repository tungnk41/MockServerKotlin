package com.mock.dao.user

import com.mock.server.application.data.database.entity.Note
import com.mock.server.application.data.database.query
import com.mock.server.application.data.database.entity.NoteEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteDaoImpl : NoteDao {
    private fun resultRowMapping(row: ResultRow) = Note(
        id = row[NoteEntity.id],
        title = row[NoteEntity.title],
        content = row[NoteEntity.content],
    )
    override suspend fun create(title: String, content: String, userId: Int): Note? = query{
        val insertStatement = NoteEntity.insert {
            it[NoteEntity.title] = title
            it[NoteEntity.content] = content
            it[NoteEntity.userId] = userId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowMapping)
    }

    override suspend fun deleteById(noteId: Int, userId: Int): Boolean = query {
        NoteEntity.deleteWhere { NoteEntity.id.eq( noteId) and NoteEntity.userId.eq(userId) } > 0
    }

    override suspend fun update(noteId: Int, title: String, content: String, userId: Int): Boolean = query {
        NoteEntity.update({ NoteEntity.id.eq( noteId) and NoteEntity.userId.eq(userId) }) {
            it[NoteEntity.title] = title
            it[NoteEntity.content] = content
        } > 0
    }

    override suspend fun findById(noteId: Int, userId: Int): Note? = query{
        NoteEntity
            .select { NoteEntity.id.eq( noteId) and NoteEntity.userId.eq(userId) }
            .map(::resultRowMapping)
            .singleOrNull()
    }

    override suspend fun findByTitle(title: String, userId: Int): List<Note>? = query {
        NoteEntity
            .select { NoteEntity.userId.eq(userId) and  NoteEntity.title.eq(title) }
            .map(::resultRowMapping)
    }

    override suspend fun findAll(userId: Int): List<Note>? = query {
        NoteEntity
            .select { NoteEntity.userId.eq(userId)}
            .map(::resultRowMapping)
    }
}