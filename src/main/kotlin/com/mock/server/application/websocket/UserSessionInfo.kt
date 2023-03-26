package com.mock.server.application.websocket

data class UserSessionInfo(
    val userId: Int,
    val username: String,
    val sessionId: String,
)