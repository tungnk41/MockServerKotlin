package com.mock.plugins

import com.mock.application.websocket.UserSocketSession
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.sessions.*
import io.ktor.util.*


fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSocketSession>("SESSION")
    }

    // Get session for current user
    intercept(Plugins) {

        if(call.sessions.get<UserSocketSession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(UserSocketSession(username = username, sessionId =  generateNonce())) // generate a session ID (nonce)
        }
    }
}
