package com.mock.dao.user

import com.mock.data.database.entity.Note
import com.mock.data.database.query
import com.mock.data.database.entity.NoteEntity
import com.mock.data.database.entity.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteDaoImpl : NoteDao {
    private fun resultRowMapping(row: ResultRow) = Note(
        id = row[NoteEntity.id],
        title = row[NoteEntity.title],
        content = row[NoteEntity.content],
    )
    override suspend fun create(note: Note, user: User): Note? = query{
        val insertStatement = NoteEntity.insert {
            it[title] = note.title
            it[content] = note.content
            it[userId] = user.id!!
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowMapping)
    }

    override suspend fun deleteById(noteId: Int, user: User): Boolean = query {
        NoteEntity.deleteWhere { NoteEntity.id eq noteId } > 0
    }

    override suspend fun updateById(noteId: Int,note: Note, user: User): Boolean = query {
        NoteEntity.update({ NoteEntity.id eq noteId }) {
            it[title] = note.title
            it[content] = note.content
        } > 0
    }

    override suspend fun findById(noteId: Int, user: User): Note? = query{
        NoteEntity
            .select { NoteEntity.id eq noteId }
            .map(::resultRowMapping)
            .singleOrNull()
    }

    override suspend fun findByTitle(title: String, user: User): List<Note>? = query {
        NoteEntity
            .select { NoteEntity.userId.eq(user.id!!) and  NoteEntity.title.eq(title) }
            .map(::resultRowMapping)
    }

    override suspend fun findAll(user: User): List<Note>? = query {
        NoteEntity
            .select { NoteEntity.userId.eq(user.id!!)}
            .map(::resultRowMapping)
    }
}