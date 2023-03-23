package com.mock.application.auth.principal

import com.mock.data.database.entity.User
import io.ktor.server.auth.*

class UserPrincipal(val user: User) : Principal