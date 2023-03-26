package com.mock.server.plugins

import com.mock.server.application.websocket.UserSessionInfo
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*


fun Application.configureSession() {

    install(Sessions) {
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretAuthKey = hex("02030405060708090a0b0c")
        cookie<UserSessionInfo>("sessionInfo") {
            cookie.maxAgeInSeconds = 172800
            cookie.path = "/socket"
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretAuthKey))
        }
    }
}
