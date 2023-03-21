package com.mock.repository

import com.mock.repository.data.User

interface UserRepository {
    suspend fun insert(user: User): User?
    suspend fun findByName(userName: String): User?
    suspend fun findById(id: Long): User?
}