package com.mock.application.controller

import com.mock.dao.user.NoteDao
import com.mock.data.database.entity.Note
import com.mock.data.database.entity.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NoteController: KoinComponent {

    private val noteDAO by inject<NoteDao>()
    suspend fun create(note: Note, user: User): Note? {
        return noteDAO.create(note, user)
    }

    suspend fun findByTitle(title: String, user: User): List<Note>? {
        return noteDAO.findByTitle(title, user)
    }

    suspend fun findAll(user: User): List<Note>? {
        return noteDAO.findAll(user)
    }
}

val noteController = NoteController()