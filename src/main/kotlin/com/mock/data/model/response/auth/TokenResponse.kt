package com.mock.data.model.response.auth

import com.mock.data.model.base.DataResponse
import com.mock.data.model.base.WrapDataResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TokenResponse(override var data: Data? = null): WrapDataResponse<TokenResponse.Data>() {
    @Serializable
    data class Data(@SerialName("access_token") val accessToken: String, @SerialName("refresh_token") val refreshToken: String): DataResponse()
}