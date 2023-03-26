package com.mock.server.plugins

import com.mock.server.application.auth.TokenManager
import com.mock.server.application.auth.principal.UserPrincipal
import com.mock.server.application.websocket.UserSessionInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val tokenManager by inject<TokenManager>()

    /**Path run : verifier will verify header Authentication, if it's passed, will go to next validate scope to validate more custom information in payload, if it's still passed, will return Principal, else go to challenge scope */

    install(Authentication) {
        jwt("api-auth-jwt") {
            verifier(tokenManager.verifier)
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val username = credential.payload.getClaim("username").asString()
                userId?.let {
                    UserPrincipal(userId,username)
                } ?: null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        jwt("socket-auth-jwt") {
            verifier(tokenManager.verifier)
            validate { _ ->
                //Because authenticate socket is manual verify, so it always does not go to this scope
                null
            }
            challenge { _, _ ->
                if (!call.request.headers["Upgrade"].equals("websocket")) call.respond(HttpStatusCode.BadRequest)
                val token = call.parameters["access_token"]
                if (token.isNullOrEmpty()) call.respond(HttpStatusCode.Unauthorized)
                else {
                    try {
                        if(!tokenManager.isTokenVerified(call, token)) return@challenge
                        val payloadData = tokenManager.getPayloadData(token)
                        payloadData?.let {
                            val userId = payloadData.userId
                            val username = payloadData.username
                            call.sessions.set(
                                UserSessionInfo(
                                    userId = userId,
                                    username = username,
                                    sessionId = generateNonce()
                                )
                            )
                        } ?: kotlin.run {
                            call.respond(HttpStatusCode.Unauthorized, "Payload JWT incorrect!")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }
            }
        }
    }
}