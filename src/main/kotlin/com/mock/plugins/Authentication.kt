package com.mock.plugins

import com.mock.application.auth.TokenManager
import com.mock.application.auth.principal.UserPrincipal
import com.mock.data.dao.user.UserDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val tokenManager by inject<TokenManager>()
    val userDAO by inject<UserDao>()

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(tokenManager.verifier)
            validate { credential ->
                credential.payload.getClaim("username").asString()?.let {
                    val user = userDAO.findByUsername(it)
                    user?.let {
                        UserPrincipal(user)
                    }
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }


}