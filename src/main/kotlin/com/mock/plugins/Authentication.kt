package com.mock.plugins

import com.mock.application.auth.TokenManager
import com.mock.application.auth.principal.UserPrincipal
import com.mock.application.websocket.UserSessionInfo
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
                userId?.let {
                    UserPrincipal(it)
                } ?: null
            }
            challenge { _, _ ->
                val header = call.request.headers["Authorization"]
                if (header.isNullOrEmpty()) call.respond(HttpStatusCode.Unauthorized, "Access token not found")
                else if ((!header.contains("Bearer", true))) call.respond(
                    HttpStatusCode.Unauthorized,
                    "JWT cannot decode token"
                )
                else {
                    val token = header.replace("Bearer ", "")
                    tokenManager.verifyAuthentication(call, token)
                }
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
                        tokenManager.verifyAuthentication(call, token)
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