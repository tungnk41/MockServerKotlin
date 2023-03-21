package com.mock.dao.user

import com.mock.models.Note
import com.mock.models.User

interface NoteDao {
    suspend fun create(note: Note, user: User): Note?
    suspend fun findByTitle(title: String, user: User): List<Note>?
    suspend fun findAll(user: User): List<Note>?
}