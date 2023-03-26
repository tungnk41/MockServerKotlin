package com.mock.client.network.impl

import com.mock.client.TokenManager
import com.mock.client.model.LoginRequest
import com.mock.client.model.TokenData
import com.mock.client.model.TokenResponse
import com.mock.client.network.AuthNetworkService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthNetworkServiceImpl(private val tokenManager: TokenManager) : AbstractNetworkServiceImpl(),
    AuthNetworkService {

    override suspend fun login(username: String, password: String): TokenResponse {
        return client.post("$BASE_URL/auth/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(LoginRequest(username = username, password = password))
        }.body()
    }

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
        val response = client.get("${BASE_URL}/auth/refresh-token") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header("Authorization", "Bearer ${tokenManager.refresh_token}")
        }
        if (response.status != HttpStatusCode.OK) return TokenResponse(data = null)
        val body = response.body<TokenResponse>()
        val accessToken = body.data?.accessToken ?: ""
        val refreshToken = body.data?.refreshToken ?: ""
        return TokenResponse(data = TokenData(accessToken, refreshToken))
    }

}