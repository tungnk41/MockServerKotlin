package com.mock.client.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val token: String,
)