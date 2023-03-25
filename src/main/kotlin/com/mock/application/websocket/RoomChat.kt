package com.mock.application.websocket

import io.ktor.websocket.*
import kotlinx.coroutines.isActive
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
            listUser[user.userId]?.let {
                sendGlobalMessage(it,"Connected from another device !")
                sessionManager.removeSession(it.sessionId)
            }
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
                is Action.SendMessageGlobal -> {
                    sendGlobalMessage(action.userSessionInfoRequest, message = action.message)
                }

                is Action.CloseRoom -> {
                    close(action.userSessionInfoSource)
                }

                else -> {
                    return
                }
            }
        }
    }

    private fun parseActionFromMsgStr(userSessionInfo: UserSessionInfo, msg: String): Action {
        val action = if (msg.startsWith("[100]")) {
            Action.SendMessageGlobal(userSessionInfo, msg.replace("[100]", ""))
        } else if (msg.startsWith("[101]")) {
            Action.CloseRoom(userSessionInfo)
        } else {
            Action.SendMessageGlobal(userSessionInfo, msg.replace("[101]", ""))
//            Action.None
        }
        return action
    }

    private suspend fun sendGlobalMessage(userSessionInfo: UserSessionInfo?, message: String) {
        val receivedMessage = userSessionInfo?.let { "${userSessionInfo.username} : $message" } ?: message
        listUser.forEach { (userId, userSessionInfo) ->
            val session = sessionManager.getSession(userSessionInfo.sessionId)
            session?.let {
                if (it.isActive) {
                    session.send(receivedMessage)
                }
            }
        }
    }

    suspend fun close(userSessionInfo: UserSessionInfo?) {
        sendGlobalMessage(userSessionInfo," request room close !!")
        listUser.forEach { (userId, userSessionInfo) ->
            sessionManager.removeSession(userSessionInfo.sessionId)
        }
        listUser.clear()
    }
}


sealed class Action {
    data class SendMessageGlobal(val userSessionInfoRequest: UserSessionInfo, val message: String) : Action()
    data class BlockUser(val userSessionInfoSource: UserSessionInfo, val userSessionInfoDes: UserSessionInfo) :
        Action()

    data class CloseRoom(val userSessionInfoSource: UserSessionInfo) : Action()
    object None : Action()
}