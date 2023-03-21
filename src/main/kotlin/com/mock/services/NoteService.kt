package com.mock.services

import com.mock.dao.user.noteDAO
import com.mock.models.Note
import com.mock.models.User
import io.ktor.server.plugins.*

class NoteService {
    suspend fun create(note: Note, user: User): Note? {
        return noteDAO.create(note, user)
    }

    suspend fun findByTitle(title: String, user: User): List<Note>? {
        val notes = noteDAO.findByTitle(title, user)
        return notes
    }

    suspend fun findAll(user: User): List<Note>? {
        val notes = noteDAO.findAll(user)
        return notes
    }
}

val noteService = NoteService()