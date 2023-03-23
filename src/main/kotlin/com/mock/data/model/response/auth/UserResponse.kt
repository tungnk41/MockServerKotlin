package com.mock.data.model.response.auth

import com.mock.data.model.base.DataResponse
import com.mock.data.model.base.WrapDataResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserResponse(override var data: Data? = null): WrapDataResponse<UserResponse.Data>() {
    @Serializable
    data class Data(val id: Int, val username: String = ""): DataResponse()
}