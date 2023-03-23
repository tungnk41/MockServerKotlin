package com.mock.application.controller

import com.mock.dao.user.NoteDao
import com.mock.data.database.entity.Note
import com.mock.data.database.entity.User
import com.mock.data.model.request.NoteRequest
import com.mock.data.model.response.note.NoteResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NoteController: KoinComponent {

    private val noteDAO by inject<NoteDao>()
    suspend fun create(request: NoteRequest, user: User): NoteResponse? {
        val note = noteDAO.create(Note(title = request.title, content = request.content), user) ?: return null
        return mapToNoteResponse(note)
    }

    suspend fun findByTitle(title: String, user: User): List<NoteResponse>? {
        return noteDAO.findByTitle(title, user)?.map { mapToNoteResponse(it)  }
    }

    suspend fun findById(noteId: Int, user: User): NoteResponse? {
        val note = noteDAO.findById(noteId, user) ?: return null
        return mapToNoteResponse(note)
    }

    suspend fun deleteById(noteId: Int, user: User): Boolean {
        return noteDAO.deleteById(noteId, user)
    }

    suspend fun update(noteRequest: NoteRequest, user: User): Boolean {
        return noteDAO.update(Note(id = noteRequest.id, title = noteRequest.title, content = noteRequest.content), user)
    }

    suspend fun findAll(user: User): List<NoteResponse>? {
        return noteDAO.findAll(user)?.map { mapToNoteResponse(it) }
    }

    private fun mapToNoteResponse(note: Note) : NoteResponse {
        return NoteResponse(id = note.id, title = note.title, content = note.content)
    }
}
