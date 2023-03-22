package com.mock.application.di

import com.mock.application.controller.AuthController
import com.mock.application.controller.NoteController
import com.mock.application.controller.UserController
import org.koin.dsl.module

val ControllerModule = module {
    single { AuthController() }
    single { UserController() }
    single { NoteController() }
}