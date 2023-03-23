package com.mock.application.controller

import com.mock.application.auth.TokenManager
import com.mock.data.model.request.LoginRequest
import com.mock.data.model.request.RefreshTokenRequest
import com.mock.data.model.request.RegisterRequest
import com.mock.data.model.response.auth.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthController: KoinComponent {

    private val tokenManager by inject<TokenManager>()
    private val userController by inject<UserController>()
    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        val user = userController.findUser(loginRequest.username,loginRequest.password)
        return user?.let {
            val token = tokenManager.generateToken(user)
             LoginResponse(UserResponse(id = user.id, username = user.username), token.accessToken,token.refreshToken)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse? {
        val user =  userController.createUser(registerRequest.username,registerRequest.password)
        return user?.let {
            val token = tokenManager.generateToken(user)
            RegisterResponse(UserResponse(id = user.id, username = user.username), token.accessToken,token.refreshToken)
        }
    }

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): TokenResponse? {
        try {
            val token = refreshTokenRequest.token
            if (!tokenManager.isVerifiedToken(token) || !tokenManager.isRefreshToken(token) || tokenManager.isExpiredToken(token)) return null
            return tokenManager.refreshToken(token)
        }
        catch (e: Exception) {
            return null
        }
    }
}