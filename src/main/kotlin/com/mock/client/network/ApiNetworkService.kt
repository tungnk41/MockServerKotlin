package com.mock.client.network

import com.mock.client.model.UserInfoResponse

interface ApiNetworkService {
    suspend fun getUserInfo(): UserInfoResponse
}