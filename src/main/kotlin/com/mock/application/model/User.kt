package com.mock.application.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int? = null, val username: String = "", val password: String = "")