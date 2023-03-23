package com.mock.dao.user

import com.mock.data.database.entity.Note
import com.mock.data.database.entity.User


interface NoteDao {
    suspend fun create(note: Note, user: User): Note?
    suspend fun deleteById(noteId: Int, user: User): Boolean
    suspend fun updateById(noteId: Int,note: Note, user: User): Boolean
    suspend fun findById(noteId: Int, user: User): Note?
    suspend fun findByTitle(title: String, user: User): List<Note>?
    suspend fun findAll(user: User): List<Note>?
}