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
        tokenManager.access_token = loginResponse.data?.accessToken ?: ""
        tokenManager.refresh_token = loginResponse.data?.refreshToken ?: ""
        println("#### Login: " + loginResponse.data)
        println("#### Token: " + tokenManager.access_token +" : " + tokenManager.refresh_token)
        apiNetworkService.getUserInfo()
    }

}

