package com.mock.application.auth.principal

import com.mock.application.models.User
import io.ktor.server.auth.*

class UserPrincipal(val user: User) : Principal