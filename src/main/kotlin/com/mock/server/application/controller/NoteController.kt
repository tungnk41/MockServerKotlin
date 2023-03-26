package com.mock.server.application.controller

import com.mock.dao.user.NoteDao
import com.mock.server.application.data.database.entity.Note
import com.mock.server.application.data.model.request.NoteRequest
import com.mock.server.application.data.model.response.note.ListNoteResponse
import com.mock.server.application.data.model.response.note.NoteResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NoteController: KoinComponent {

    private val noteDAO by inject<NoteDao>()
    suspend fun create(request: NoteRequest, userId: Int): NoteResponse {
        val response = noteDAO.create(title = request.title, content = request.content, userId)
        return createNoteResponse(response)
    }

    suspend fun findByTitle(title: String, userId: Int): ListNoteResponse {
        val response = noteDAO.findByTitle(title, userId)
        return createListNoteResponse(response)
    }

    suspend fun findById(noteId: Int, userId: Int): NoteResponse {
        val response = noteDAO.findById(noteId, userId)
        return createNoteResponse(response)
    }

    suspend fun deleteById(noteId: Int, userId: Int): Boolean {
        return noteDAO.deleteById(noteId, userId)
    }

    suspend fun update(noteRequest: NoteRequest, userId: Int): Boolean {
        if (noteRequest.id == null) return false
        return noteDAO.update(noteId = noteRequest.id, title = noteRequest.title, content = noteRequest.content, userId)
    }

    suspend fun findAll(userId: Int): ListNoteResponse {
        val response = noteDAO.findAll(userId)
        return createListNoteResponse(response)
    }

    private fun createNoteResponse(note: Note?): NoteResponse {
        return NoteResponse().apply {
            note?.let {
                data = mapToNoteResponseData(it)
            }
        }
    }

    private fun createListNoteResponse(listNotes: List<Note>?): ListNoteResponse {
        return ListNoteResponse().apply {
            listNotes?.let {
                data = mapToListNoteResponseData(it)
            }
        }
    }

    private fun mapToNoteResponseData(note: Note) : NoteResponse.Data {
        return NoteResponse.Data(id = note.id, title = note.title, content = note.content)
    }

    private fun mapToListNoteResponseData(listNotes: List<Note>) : List<NoteResponse.Data> {
        return listNotes.map { mapToNoteResponseData(it) }
    }
}
