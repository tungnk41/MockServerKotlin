package com.mock.repository

import com.mock.repository.data.Todo

interface TodoRepository {
    suspend fun insert(userId: Long, todo: Todo): Todo?
    suspend fun findByTitle(userId: Long, title: String): Todo?
    suspend fun find(userId: Long): List<Todo>?
    suspend fun deleteByID(userId: Long, id: Long): Int
}