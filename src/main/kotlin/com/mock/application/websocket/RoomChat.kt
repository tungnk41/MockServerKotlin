package com.mock.application.websocket

import io.ktor.websocket.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.ConcurrentHashMap

class RoomChat : KoinComponent {
    private val sessionManager by inject<SessionManager>()
    private val listUser = ConcurrentHashMap<Int, UserSessionInfo>(hashMapOf())
    private val listBlockedUser = ConcurrentHashMap<Int, UserSessionInfo>(hashMapOf())


    fun isUserBlocked(user: UserSessionInfo): Boolean {
        return listBlockedUser.containsKey(user.userId)
    }

    suspend fun addUser(user: UserSessionInfo, session: DefaultWebSocketSession) {
        if (listUser.containsKey(user.userId)) {
            listUser[user.userId]?.let { sessionManager.removeSession(it.sessionId) }
            listUser.remove(user.userId)
        }
        listUser[user.userId] = user
        sessionManager.addSession(user.sessionId, session)
    }

    suspend fun removeUserById(userId: Int) {
        if (listUser.containsKey(userId)) {
            listUser[userId]?.sessionId?.let { sessionManager.removeSession(it) }
            listUser.remove(userId)
        }
    }


    suspend fun handleAction(userId: Int, message: String) {
        listUser[userId]?.let {
            when (val action = parseActionFromMsgStr(it, message)) {
                is Action.SendMessageToAll -> {
                    sendMessageToAll(action.userSessionInfoRequest, message = action.message)
                }

                is Action.DisconnectAll -> {
                    disconnectAll(action.userSessionInfoSource)
                }

                else -> {
                    return
                }
            }
        }
    }

    private fun parseActionFromMsgStr(userSessionInfo: UserSessionInfo, msg: String): Action {
        val action = if (msg.startsWith("[ALL][MSG]")) {
            Action.SendMessageToAll(userSessionInfo, msg.replace("[ALL][MSG]", ""))
        } else if (msg.startsWith("[ALL][CLOSE]")) {
            Action.DisconnectAll(userSessionInfo)
        } else {
            Action.SendMessageToAll(userSessionInfo, msg.replace("[ALL][MSG]", ""))
//            Action.None
        }
        return action
    }

    private suspend fun sendMessageToAll(userSessionInfo: UserSessionInfo, message: String) {
        val receivedMessage = "${userSessionInfo.username} : $message"
        listUser.forEach { (userId, userSessionInfo) ->
            val session = sessionManager.getSession(userSessionInfo.sessionId)
            session?.send(receivedMessage)
        }
    }

    private suspend fun disconnectAll(userSessionInfo: UserSessionInfo) {
        listUser.forEach { (userId, userSessionInfo) ->
            sessionManager.removeSession(userSessionInfo.sessionId)
        }
        listUser.clear()
    }
}


sealed class Action {
    data class SendMessageToAll(val userSessionInfoRequest: UserSessionInfo, val message: String) : Action()
    data class DisconnectUser(val userSessionInfoSource: UserSessionInfo, val userSessionInfoDes: UserSessionInfo) :
        Action()

    data class DisconnectAll(val userSessionInfoSource: UserSessionInfo) : Action()
    object None : Action()
}