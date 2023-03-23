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
        val newUser = User(username = username, password = encryptPassword)
        return userDAO.createUser(newUser)
    }

    suspend fun findUser(username: String, password: String): User? {
        val response = userDAO.findByUsername(username = username)
        return if (response != null && passwordEncryptor.validatePassword(
                password,
                response.password
            )
        ) response else null
    }
}