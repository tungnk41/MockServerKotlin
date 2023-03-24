package com.mock.application.websocket

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

sealed class Action {
    data class SendMessageToAll(val fromUserSessionId: String,val message: String): Action()
    data class SendMessageToUser(val fromUserSessionId: String, val toUserSessionId: String, val message: String): Action()
    data class RemoveUser(val userSessionId: String): Action()
    object RemoveAllUser: Action()
    object None: Action()
}

class RoomChat {
    private val userConnections = ConcurrentHashMap<String,UserSocketSession>(hashMapOf())

    private suspend fun sendMessageToAll(fromUser: UserSocketSession, message: String) {
        val receivedMessage = "${fromUser.username} : $message"
        userConnections.forEach { (userSessionId, userSocketSession) ->
            userSocketSession.session?.send(receivedMessage)
        }
    }

    suspend fun addUser(user: UserSocketSession) {
        if (!userConnections.containsKey(user.sessionId)) {
            userConnections[user.sessionId] = user
        }
    }

    suspend fun removeUser(user: UserSocketSession) {
        if (userConnections.containsKey(user.sessionId)) {
            userConnections.remove(user.sessionId)
        }
    }

    suspend fun handleAction(fromUserSessionId: String, message: String){
        val action = if (message.startsWith("[ALL][MSG]")) {
            Action.SendMessageToAll(fromUserSessionId, message.replace("[ALL][MSG]",""))
        }
        else if (message.startsWith("[ALL][CLOSE]")) {
            Action.RemoveAllUser
        }
        else{
            Action.SendMessageToAll(fromUserSessionId, message.replace("[ALL][MSG]",""))
//            Action.None
        }

        when(action) {
            is Action.SendMessageToAll -> {
                val user = userConnections[action.fromUserSessionId] ?: return
                sendMessageToAll(fromUser = user, message = action.message)
            }
            is Action.RemoveAllUser -> {
                userConnections.forEach {(K,V) ->
                    V.session?.close()
                }
                userConnections.clear()
            }
            else -> {
                return
            }
        }
    }

}