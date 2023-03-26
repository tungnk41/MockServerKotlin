package com.mock.server.application.di

import com.mock.server.application.websocket.RoomChat
import com.mock.server.application.websocket.SessionManager
import org.koin.dsl.module

val WebSocketModule = module {
    factory { RoomChat() }
    single { SessionManager() }
}