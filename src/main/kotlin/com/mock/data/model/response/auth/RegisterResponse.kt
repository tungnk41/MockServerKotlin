package com.mock.data.model.response.auth

import com.mock.data.model.base.DataResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(val user: UserResponse, @SerialName("access_token") val accessToken: String, @SerialName("refresh_token") val refreshToken: String): DataResponse()