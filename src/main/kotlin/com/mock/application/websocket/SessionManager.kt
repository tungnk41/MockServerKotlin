package com.mock.application.websocket

import io.ktor.websocket.*
import org.koin.core.component.KoinComponent
import java.util.concurrent.ConcurrentHashMap

class SessionManager: KoinComponent {
    private val sessions = ConcurrentHashMap<String,DefaultWebSocketSession>(hashMapOf())

    suspend fun addSession(sessionId: String,session: DefaultWebSocketSession) {
        if (!sessions.containsKey(sessionId)) {
            sessions[sessionId] = session
        }
    }

    suspend fun getSession(sessionId: String) : DefaultWebSocketSession? {
        return sessions[sessionId]
    }

    suspend fun removeSession(sessionId: String) {
        if (sessions.containsKey(sessionId)) {
            sessions[sessionId]?.close()
            sessions.remove(sessionId)
        }
    }

    fun getSize(): Int {
        return sessions.size
    }

    suspend fun clearAll() {
        sessions.forEach {(_,session) ->
            session.close()
        }
    }

}