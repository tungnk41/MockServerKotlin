package com.mock.data.model.response.auth

import com.mock.data.model.base.DataResponse
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val id: Int? = null, val username: String = ""): DataResponse()