package com.mock.routes


import com.mock.application.websocket.RoomChat
import com.mock.application.websocket.SessionManager
import com.mock.application.websocket.UserSessionInfo
import io.ktor.websocket.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.ktor.ext.inject

fun Route.webSocketRoute() {
        val publicRoom by inject<RoomChat>()
        val sessionManager by inject<SessionManager>()

        webSocket("/public") {
            val sessionInfo = call.sessions.get<UserSessionInfo>()
            if(sessionInfo == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session info."))
                return@webSocket
            }
            val userSessionInfo = UserSessionInfo(sessionInfo.userId,sessionInfo.username,sessionInfo.sessionId)
            publicRoom.addUser(userSessionInfo, this)

            try {
                // Listen for incoming messages using channel coroutine
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text) {
                        publicRoom.handleAction(userSessionInfo.userId,frame.readText())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                //Run when user is disconnected
            }
        }
}