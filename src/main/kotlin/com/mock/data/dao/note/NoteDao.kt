package com.mock.dao.user

import com.mock.data.database.entity.Note


interface NoteDao {
    suspend fun create(note: Note, userId: Int): Note?
    suspend fun update(note: Note, userId: Int): Boolean
    suspend fun deleteById(noteId: Int, userId: Int): Boolean
    suspend fun findById(noteId: Int, userId: Int): Note?
    suspend fun findByTitle(title: String, userId: Int): List<Note>?
    suspend fun findAll(userId: Int): List<Note>?
}