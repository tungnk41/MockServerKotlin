package com.mock.repository.data.source

import com.mock.repository.data.User

interface UserDataSource {
    suspend fun insert(user: User): User?
    suspend fun update(user: User): Int
    suspend fun findById(id: Long): User?
    suspend fun findByName(username: String): User?
    suspend fun find() : List<User>?
    suspend fun deleteById(id: Long): Int
}