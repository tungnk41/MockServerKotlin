package com.mock.application.auth.principal

import io.ktor.server.auth.*

class UserPrincipal(val userId: Int) : Principal