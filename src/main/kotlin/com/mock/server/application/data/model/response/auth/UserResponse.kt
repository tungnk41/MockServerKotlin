package com.mock.server.application.data.model.response.auth

import com.mock.server.application.data.model.base.DataResponse
import com.mock.server.application.data.model.base.WrapDataResponse
import kotlinx.serialization.Serializable

@Serializable
class UserResponse(override var data: Data? = null): WrapDataResponse<UserResponse.Data>() {
    @Serializable
    data class Data(val id: Int, val username: String = ""): DataResponse()
}