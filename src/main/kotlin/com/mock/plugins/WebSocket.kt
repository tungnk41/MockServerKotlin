package com.mock.plugins

import com.mock.application.auth.TokenManager
import com.mock.application.websocket.UserSessionInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
import java.time.Duration


fun Application.configureWebsocket() {
    val tokenManager by inject<TokenManager>()

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(Sessions) {
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretAuthKey = hex("02030405060708090a0b0c")
        cookie<UserSessionInfo>("Session") {
            cookie.maxAgeInSeconds = 172800
            cookie.path = "/socket"
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretAuthKey))
        }
    }

    // Get session for current user
    intercept(ApplicationCallPipeline.Plugins) {
        if (!call.request.headers["Upgrade"].equals("websocket")) return@intercept

        if (call.sessions.get<UserSessionInfo>() == null) {
            val token = call.parameters["access_token"]
            if (token.isNullOrEmpty()) call.respond(HttpStatusCode.Unauthorized)
            else {
                try {
                    tokenManager.verifyAuthentication(call, token)
                    val payloadData = tokenManager.getPayloadData(token)
                    payloadData?.let {
                        val userId = payloadData.userId
                        val username = payloadData.username
                        call.sessions.set(UserSessionInfo(userId = userId, username = username, sessionId = generateNonce()))
                    } ?: kotlin.run { call.respond(HttpStatusCode.Unauthorized,"Payload JWT incorrect!") }
                }
                catch (e: Exception) {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
    }
}
