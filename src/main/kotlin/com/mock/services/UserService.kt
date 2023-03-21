package com.mock.services

import com.mock.dao.user.userDAO
import com.mock.models.User
import com.mock.utils.Hasher
import io.ktor.server.plugins.*

class UserService {
    suspend fun createUser(user: User): User? {
        val hashedPassword = Hasher.hashPassword(user.password)
        val newUser = User(username = user.username, password = hashedPassword)
        return userDAO.createUser(newUser)
    }

    suspend fun loginUser(username: String, password: String): User? {
        val user = userDAO.findByUsername(username)
        return if (user != null && Hasher.checkPassword(password, user)) user else null
    }

}
val userService = UserService()