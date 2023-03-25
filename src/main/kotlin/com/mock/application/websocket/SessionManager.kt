package com.mock.application.websocket

import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import org.koin.core.component.KoinComponent
import java.util.concurrent.ConcurrentHashMap

class SessionManager: KoinComponent {
    private val sessions = ConcurrentHashMap<String,DefaultWebSocketSession>(hashMapOf())

    suspend fun addSession(sessionId: String,session: DefaultWebSocketSession) {
        if (!sessions.containsKey(sessionId)) {
            sessions[sessionId] = session
        }
        scanTotalSession()
    }

    suspend fun getSession(sessionId: String) : DefaultWebSocketSession? {
        return sessions[sessionId]
    }

    suspend fun removeSession(sessionId: String) {
        if (sessions.containsKey(sessionId)) {
            sessions[sessionId]?.close()
            sessions.remove(sessionId)
        }
        scanTotalSession()
    }

    fun getSize(): Int {
        return sessions.size
    }

    suspend fun clearAll() {
        sessions.forEach {(_,session) ->
            session.close()
        }
    }

    private suspend fun scanTotalSession() {
        if (sessions.size > 0) {
            println("SessionManager Print Session : Size: ${sessions.size}")
            sessions.forEach {
                println("SessionManager Print Session : SessionId: " + it.key)
                if (!it.value.isActive) {
                    removeSession(it.key)
                }
            }
        }
        else {
            println("SessionManager Print Session : Size: 0")
        }
    }

}