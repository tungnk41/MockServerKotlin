package com.mock.server.application.data.di

import com.mock.dao.user.NoteDao
import com.mock.dao.user.NoteDaoImpl
import com.mock.server.application.data.database.dao.user.UserDao
import com.mock.server.application.data.database.dao.user.UserDaoImpl
import org.koin.dsl.module

val DaoModule = module {
    single<NoteDao> { NoteDaoImpl() }
    single<UserDao> { UserDaoImpl() }
}