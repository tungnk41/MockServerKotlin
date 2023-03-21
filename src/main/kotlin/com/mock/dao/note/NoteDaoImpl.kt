package com.mock.dao.user

import com.mock.dao.DatabaseFactory.query
import com.mock.models.Note
import com.mock.models.NoteEntity
import com.mock.models.User
import org.jetbrains.exposed.sql.*

class NoteDaoImpl : NoteDao {
    private fun resultRowMapping(row: ResultRow) = Note(
        id = row[NoteEntity.id],
        title = row[NoteEntity.title],
        content = row[NoteEntity.content],
    )
    override suspend fun create(note: Note, user: User): Note? = query{
        println("#### " + note + " , " + user)
        val insertStatement = NoteEntity.insert {
            it[NoteEntity.title] = note.title
            it[NoteEntity.content] = note.content
            it[NoteEntity.userId] = user.id
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowMapping)
    }

    override suspend fun findByTitle(title: String, user: User): List<Note>? = query {
        NoteEntity
            .select { NoteEntity.userId.eq(user.id) and  NoteEntity.title.eq(title) }
            .map(::resultRowMapping)
    }

    override suspend fun findAll(user: User): List<Note>? = query {
        NoteEntity
            .select { NoteEntity.userId.eq(user.id)}
            .map(::resultRowMapping)
    }
}

val noteDAO = NoteDaoImpl()