package com.mock.plugins

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.mock.application.auth.TokenManager
import com.mock.application.auth.principal.UserPrincipal
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val tokenManager by inject<TokenManager>()

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(tokenManager.verifier)
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                userId?.let {
                    UserPrincipal(it)
                } ?: null
            }
            challenge { _, _ ->
                val header = call.request.headers["Authorization"]
                if (header.isNullOrEmpty())  call.respond(HttpStatusCode.Unauthorized, "Access token not found")
                else if ((!header.contains("Bearer", true)))  call.respond(HttpStatusCode.Unauthorized, "JWT cannot decode token")
                else {
                    val token = header.replace("Bearer ", "")
                    tokenManager.verifyAuthentication(call,token)
                }
            }
        }
    }
}