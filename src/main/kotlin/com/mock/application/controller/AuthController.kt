package com.mock.application.controller

import com.mock.application.auth.TokenManager
import com.mock.data.database.dao.user.UserDao
import com.mock.data.model.request.LoginRequest
import com.mock.data.model.request.RefreshTokenRequest
import com.mock.data.model.request.RegisterRequest
import com.mock.data.model.response.auth.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthController: KoinComponent {

    private val tokenManager by inject<TokenManager>()
    private val userController by inject<UserController>()

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        val response = userController.loginUser(username = loginRequest.username, password = loginRequest.password)
        return createLoginResponse(response.data,tokenManager)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        val response =  userController.createUser(registerRequest.username,registerRequest.password)
        return createRegisterResponse(response.data,tokenManager)
    }

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): TokenResponse? {
        try {
            val token = refreshTokenRequest.token
            if (!tokenManager.isVerifiedToken(token) || !tokenManager.isRefreshToken(token) || tokenManager.isExpiredToken(token)) return null
            return TokenResponse().apply { data = tokenManager.refreshToken(token)}
        }
        catch (e: Exception) {
            return null
        }
    }

    private fun createLoginResponse(userData: UserResponse.Data?, tokenManager: TokenManager) : LoginResponse {
        return LoginResponse().apply {
            userData?.let {
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