package com.mock.repository.data

import io.ktor.auth.Principal
import java.io.Serializable

data class User(val id : Long?, val username: String, val password: String) : Serializable, Principal