package com.mock.data.dao.user

import com.mock.application.models.User

interface UserDao {
    suspend fun createUser(user: User): User?
    suspend fun updateById(userId: Int,user: User): Boolean
    suspend fun deleteById(userId: Int): Boolean
    suspend fun findById(userId: Int): User?
    suspend fun findByUsername(username: String): User?
}