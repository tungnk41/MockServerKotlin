package com.mock.client

import com.mock.client.network.ApiNetworkService
import com.mock.client.network.AuthNetworkService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Script : KoinComponent{

    val apiNetworkService by inject<ApiNetworkService>()
    val authNetworkService by inject<AuthNetworkService>()
    val tokenManager by inject<TokenManager>()
    suspend fun run() {
        val loginResponse = authNetworkService.login("kama","123@123a")
        tokenManager.setAccessToken(loginResponse.data?.accessToken ?: "")
        tokenManager.setRefreshToken(loginResponse.data?.refreshToken ?: "")
        apiNetworkService.getUserInfo()
    }

}

