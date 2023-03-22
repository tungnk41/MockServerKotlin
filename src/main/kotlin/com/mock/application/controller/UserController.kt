package com.mock.application.controller

import com.mock.application.auth.PasswordEncryptor
import com.mock.application.models.User
import com.mock.data.dao.user.UserDao
import com.mock.data.model.request.RegisterRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserController : KoinComponent {

    private val passwordEncryptor by inject<PasswordEncryptor>()
    private val userDAO by inject<UserDao>()
    suspend fun createUser(registerRequest: RegisterRequest): User? {
        val encryptPassword = passwordEncryptor.encryptPassword(registerRequest.password)
        val newUser = User(username = registerRequest.username, password = encryptPassword)
        return userDAO.createUser(newUser)
    }

    suspend fun findUser(user: User): User? {
        val response = userDAO.findByUsername(username = user.username)
        return if (response != null && passwordEncryptor.validatePassword(
                user.password,
                response.password
            )
        ) user else null
    }
}