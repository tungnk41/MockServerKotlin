package com.mock.server.application.data.model.response.auth

import com.mock.server.application.data.model.base.DataResponse
import com.mock.server.application.data.model.base.WrapDataResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RegisterResponse(override var data: Data? = null) : WrapDataResponse<RegisterResponse.Data>() {
    @Serializable
    data class Data(val user: UserResponse.Data, @SerialName("access_token") val accessToken: String, @SerialName("refresh_token") val refreshToken: String): DataResponse()
}