package com.mock.server.application.di

import com.mock.server.application.controller.AuthController
import com.mock.server.application.controller.NoteController
import com.mock.server.application.controller.UserController
import org.koin.dsl.module

val ControllerModule = module {
    single { AuthController() }
    single { UserController() }
    single { NoteController() }
}