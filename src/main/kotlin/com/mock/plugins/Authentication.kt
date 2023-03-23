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

fun Application.configureAuthentication() {
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
                header?.let {
                    if (it.isNotEmpty()) {
                        try {
                            if ((!it.contains("Bearer", true))) throw JWTDecodeException("")
                            val jwt = it.replace("Bearer ", "")
                            tokenManager.verifier.verify(jwt)
                            ""
                        } catch (e: TokenExpiredException) {
                            call.respond(HttpStatusCode.Unauthorized,"TokenExpiredException: Token Expired")
                        } catch (e: SignatureVerificationException) {
                            call.respond(HttpStatusCode.BadRequest, "SignatureVerificationException: Failed to parse Access token")
                        } catch (e: JWTDecodeException) {
                            call.respond(HttpStatusCode.BadRequest, "JWTDecodeException: Failed to decode Access token")
                        }
                    } else call.respond(HttpStatusCode.BadRequest, "Access token not found")
                } ?: call.respond(HttpStatusCode.Unauthorized, "No authorization header found")
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}