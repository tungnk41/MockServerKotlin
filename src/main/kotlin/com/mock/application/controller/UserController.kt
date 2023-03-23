package com.mock.application.controller

import com.mock.application.auth.PasswordEncryptor
import com.mock.data.dao.user.UserDao
import com.mock.data.database.entity.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserController : KoinComponent {

    private val passwordEncryptor by inject<PasswordEncryptor>()
    private val userDAO by inject<UserDao>()
    suspend fun createUser(username: String, password: String): User? {
        val encryptPassword = passwordEncryptor.encryptPassword(password)
        return userDAO.createUser(User(username = username, password = encryptPassword))
    }

    suspend fun findUser(username: String, password: String): User? {
        val user = userDAO.findByUsername(username = username) ?: return null
        return if (passwordEncryptor.validatePassword(password, user.password)) user else null
    }

    suspend fun findUserById(userId: Int): User? {
        return userDAO.findById(userId = userId)
    }
}