package com.mock.server.application.controller

import com.mock.server.application.auth.PasswordEncryptor
import com.mock.server.application.data.database.dao.user.UserDao
import com.mock.server.application.data.database.entity.User
import com.mock.server.application.data.model.response.auth.UserResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserController : KoinComponent {

    private val passwordEncryptor by inject<PasswordEncryptor>()
    private val userDAO by inject<UserDao>()
    suspend fun createUser(username: String, password: String): UserResponse {
        val encryptPassword = passwordEncryptor.encryptPassword(password)
        val response = userDAO.createUser(username = username, password = encryptPassword)
        return createUserResponse(response)
    }

    suspend fun loginUser(username: String, password: String): UserResponse {
        val response = userDAO.findByUsername(username = username)
        return response?.password?.let {
            val isValidPassword = passwordEncryptor.validatePassword(password,response.password)
            if (isValidPassword) createUserResponse(response) else UserResponse()
        } ?: kotlin.run { UserResponse() }
    }

    suspend fun findUserById(userId: Int): UserResponse {
        val response = userDAO.findById(userId = userId)
        return  createUserResponse(response)
    }

    private fun createUserResponse(user: User?): UserResponse {
        return UserResponse().apply {
            user?.let {
                data = UserResponse.Data(user.id, user.username)
            }
        }
    }
}