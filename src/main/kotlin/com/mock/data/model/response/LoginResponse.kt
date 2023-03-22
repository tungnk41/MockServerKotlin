package com.mock.data.model.response

import com.mock.application.model.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val user: User, val access_token: String, val refresh_token: String)