package com.mock.data.model.response

import com.mock.application.models.User
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(val user: User, val access_token: String, val refresh_token: String)