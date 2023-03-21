package com.mock.dao.user

import com.mock.models.User

interface UserDao {
    suspend fun createUser(user:User): User?
    suspend fun findByUsername(username: String): User?
}