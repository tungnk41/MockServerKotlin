package com.mock.data.dao.user

import com.mock.data.database.entity.User


interface UserDao {
    suspend fun createUser(user: User): User?
    suspend fun update(user: User): Boolean
    suspend fun deleteById(userId: Int): Boolean
    suspend fun findById(userId: Int): User?
    suspend fun findByUsername(username: String): User?
    suspend fun findAll(): List<User>?
}