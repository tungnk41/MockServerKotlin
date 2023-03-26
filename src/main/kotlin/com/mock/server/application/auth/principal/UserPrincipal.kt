package com.mock.server.application.auth.principal

import io.ktor.server.auth.*

class UserPrincipal(val userId: Int, val username: String) : Principal