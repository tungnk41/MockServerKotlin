package com.mock.repository.RepositoryImpl

import com.mock.repository.TodoRepository
import com.mock.repository.data.Todo
import com.mock.repository.data.source.TodoDataSource

class TodoRepository(val localDataSource: TodoDataSource) : TodoRepository {
    override suspend fun insert(userId: Long, todo: Todo): Todo? {
        return localDataSource.insert(userId = userId,todo)
    }

    override suspend fun findByTitle(userId: Long, title: String): Todo? {
        return localDataSource.findByTitle(userId = userId,title)
    }

    override suspend fun find(userId: Long): List<Todo>? {
        return localDataSource.find(userId)
    }

    override suspend fun deleteByID(userId: Long, id: Long): Int {
        return localDataSource.deleteById(userId,id)
    }
}