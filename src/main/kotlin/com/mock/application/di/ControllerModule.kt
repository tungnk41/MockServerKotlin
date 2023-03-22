package com.mock.application.di

import com.mock.application.controller.AuthController
import com.mock.application.controller.NoteController
import com.mock.application.controller.UserController
import com.mock.dao.user.NoteDao
import com.mock.dao.user.NoteDaoImpl
import com.mock.data.dao.user.UserDao
import com.mock.data.dao.user.UserDaoImpl
import org.koin.dsl.module

val ControllerModule = module {
    single { AuthController() }
    single { UserController() }
    single { NoteController() }
}