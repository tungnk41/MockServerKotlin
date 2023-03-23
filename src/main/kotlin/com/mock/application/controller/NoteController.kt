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
    suspend fun create(request: NoteRequest, userId: Int): NoteResponse? {
        val note = noteDAO.create(Note(title = request.title, content = request.content), userId) ?: return null
        return mapToNoteResponse(note)
    }

    suspend fun findByTitle(title: String, userId: Int): List<NoteResponse>? {
        return noteDAO.findByTitle(title, userId)?.map { mapToNoteResponse(it)  }
    }

    suspend fun findById(noteId: Int, userId: Int): NoteResponse? {
        val note = noteDAO.findById(noteId, userId) ?: return null
        return mapToNoteResponse(note)
    }

    suspend fun deleteById(noteId: Int, userId: Int): Boolean {
        return noteDAO.deleteById(noteId, userId)
    }

    suspend fun update(noteRequest: NoteRequest, userId: Int): Boolean {
        return noteDAO.update(Note(id = noteRequest.id, title = noteRequest.title, content = noteRequest.content), userId)
    }

    suspend fun findAll(userId: Int): List<NoteResponse>? {
        return noteDAO.findAll(userId)?.map { mapToNoteResponse(it) }
    }

    private fun mapToNoteResponse(note: Note) : NoteResponse {
        return NoteResponse(id = note.id, title = note.title, content = note.content)
    }
}
