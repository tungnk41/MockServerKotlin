package com.mock.routes


import com.mock.application.websocket.RoomChat
import com.mock.application.websocket.UserSocketSession
import io.ktor.websocket.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.webSocketRoute() {
        val roomChat = RoomChat()

        webSocket("") {
            val session = call.sessions.get<UserSocketSession>()
            if(session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
                return@webSocket
            }

            try {
                val userSocketSession = UserSocketSession(session.username,session.sessionId,this)
                roomChat.addUser(userSocketSession)

                // Listen for incoming messages using channel coroutine
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text) {
                        roomChat.handleAction(userSocketSession.sessionId,frame.readText())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }
        }
}