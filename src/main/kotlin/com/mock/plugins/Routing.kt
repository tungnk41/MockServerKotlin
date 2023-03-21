package com.mock.plugins


import com.mock.routes.auth
import com.mock.routes.note
import io.ktor.server.application.*


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(config: ApplicationConfig) {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authenticate("auth-jwt") {
            note(config)
        }
        auth(config)
    }
}
