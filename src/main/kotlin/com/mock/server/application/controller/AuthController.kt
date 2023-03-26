package com.mock.server.application.controller

import com.mock.server.application.auth.TokenManager
import com.mock.server.application.data.model.request.LoginRequest
import com.mock.server.application.data.model.request.RefreshTokenRequest
import com.mock.server.application.data.model.request.RegisterRequest
import com.mock.server.application.data.model.response.auth.LoginResponse
import com.mock.server.application.data.model.response.auth.RegisterResponse
import com.mock.server.application.data.model.response.auth.TokenResponse
import com.mock.server.application.data.model.response.auth.UserResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthController: KoinComponent {

    private val tokenManager by inject<TokenManager>()
    private val userController by inject<UserController>()

    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        val response = userController.loginUser(username = loginRequest.username, password = loginRequest.password)
        return createLoginResponse(response.data,tokenManager)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        val response =  userController.createUser(registerRequest.username,registerRequest.password)
        return createRegisterResponse(response.data,tokenManager)
    }

    suspend fun refreshToken(userId: Int, username: String): TokenResponse? {
        return try {
            TokenResponse().apply { data = tokenManager.generateToken(userId,username)}
        } catch (e: Exception) {
            null
        }
    }

    private fun createLoginResponse(userData: UserResponse.Data?, tokenManager: TokenManager) : LoginResponse? {
        return userData?.let {
            LoginResponse().apply {
                val token = tokenManager.generateToken(it.id,it.username)
                data = LoginResponse.Data(user = UserResponse.Data(id = it.id, username = it.username), token.accessToken,token.refreshToken)
            }
        }
    }

    private fun createRegisterResponse(userData: UserResponse.Data?, tokenManager: TokenManager) : RegisterResponse {
        return RegisterResponse().apply {
            userData?.let {
                val token = tokenManager.generateToken(it.id,it.username)
                data = RegisterResponse.Data(user = UserResponse.Data(id = it.id, username = it.username), token.accessToken,token.refreshToken)
            }
        }
    }
}