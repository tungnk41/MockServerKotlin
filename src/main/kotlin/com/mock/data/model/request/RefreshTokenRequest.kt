package com.mock.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val token: String)