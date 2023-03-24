package com.mock.application.websocket

import io.ktor.websocket.*

data class UserSocketSession(
    val username: String,
    val sessionId: String,
    var session: DefaultWebSocketSession?= null
)