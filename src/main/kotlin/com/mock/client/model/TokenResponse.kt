package com.mock.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class TokenResponse(
   val data : TokenData?
)

@Serializable
data class TokenData(
    @SerialName("access_token")
    val accessToken: String?,
    @SerialName("refresh_token")
    val refreshToken: String?
)