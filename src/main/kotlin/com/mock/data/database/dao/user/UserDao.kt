package com.mock.data.database.dao.user

import com.mock.data.database.entity.User


interface UserDao {
    suspend fun createUser(username: String,password: String): User?
    suspend fun update(userId: Int,username: String,password: String): Boolean
    suspend fun deleteById(userId: Int): Boolean
    suspend fun findById(userId: Int): User?
    suspend fun findByUsername(username: String): User?
    suspend fun findAll(): List<User>?
}