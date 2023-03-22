package com.mock.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(val username:String, val password: String)