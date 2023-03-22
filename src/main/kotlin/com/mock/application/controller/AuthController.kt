package com.mock.application.controller

import com.mock.application.auth.TokenManager
import com.mock.application.models.User
import com.mock.data.model.request.LoginRequest
import com.mock.data.model.request.RegisterRequest
import com.mock.data.model.response.LoginResponse
import com.mock.data.model.response.RegisterResponse
import com.mock.data.model.response.TokenResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthController: KoinComponent {

    private val tokenManager by inject<TokenManager>()
    private val userController by inject<UserController>()
    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        val user = userController.findUser(User(username = loginRequest.username, password = loginRequest.password))
        return user?.let {
            val token = tokenManager.generateToken(user)
             LoginResponse(user, token.access_token,token.refresh_token)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse? {
        val user =  userController.createUser(registerRequest)
        return user?.let {
            val token = tokenManager.generateToken(user)
            RegisterResponse(user, token.access_token,token.refresh_token)
        }
    }
}