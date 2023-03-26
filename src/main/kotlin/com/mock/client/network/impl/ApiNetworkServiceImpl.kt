package com.mock.client.network.impl

import com.mock.client.TokenManager
import com.mock.client.model.UserInfoResponse
import com.mock.client.network.ApiNetworkService
import com.mock.client.network.AuthNetworkService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*

class ApiNetworkServiceImpl (private val tokenManager: TokenManager, private val authNetworkService: AuthNetworkService) : AbstractNetworkServiceImpl(),ApiNetworkService {

    override val client: HttpClient = super.client.apply {
        this.config {
            HttpResponseValidator {
                validateResponse { response ->
                    when(response.status.value) {
                        else -> {
                            println("#### ApiClient:  " + response.status.value)
                        }
                    }
                }
            }
            install(Auth) {
                bearer {
                    /* sendRequestWithoutToken */
//                sendWithoutRequest { !it.url.pathSegments.contains("auth") }
                    /* invoke when sendRequestWithoutToken return false */
                    loadTokens {
                        BearerTokens(tokenManager.access_token, tokenManager.refresh_token)
                    }
                    /* invoke when get 401 from server after trying refreshToken from loadTokens */
                    refreshTokens {
                        val response = authNetworkService.refreshToken(tokenManager.refresh_token).data
                        BearerTokens(response?.accessToken ?: "", response?.refreshToken ?: "")
                    }
                }
            }
        }
    }


    override suspend fun getUserInfo(): UserInfoResponse {
        return client.get("$BASE_URL/auth/refresh-token").body<UserInfoResponse>()
    }
}