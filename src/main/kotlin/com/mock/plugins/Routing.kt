package com.mock.plugins

import com.mock.auth.JwtService
import com.mock.auth.hash
import com.mock.repository.RepositoryImpl.TodoRepository
import com.mock.repository.RepositoryImpl.UserRepository
import com.mock.repository.data.source.local.TodoLocalDataSource
import com.mock.repository.data.source.local.UserLocalDataSource
import com.mock.routes.TodoRoute
import com.mock.routes.UserRoute
import io.ktor.routing.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {
    install(Locations) {
    }

    val hashFunction = { s: String -> hash(s) }
    routing {
        UserRoute(UserRepository(UserLocalDataSource()), JwtService(), hashFunction)
        TodoRoute(TodoRepository(TodoLocalDataSource()))

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
