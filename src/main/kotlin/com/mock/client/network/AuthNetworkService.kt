package com.mock.client.network

import com.mock.client.model.TokenResponse

interface AuthNetworkService {
    suspend fun login(username: String, password: String): TokenResponse
    suspend fun refreshToken(refreshToken: String): TokenResponse
}