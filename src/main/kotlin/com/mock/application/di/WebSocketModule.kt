package com.mock.application.di

import com.mock.application.websocket.RoomChat
import com.mock.application.websocket.SessionManager
import org.koin.dsl.module

val WebSocketModule = module {
    factory { RoomChat() }
    single { SessionManager() }
}