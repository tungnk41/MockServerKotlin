package com.mock.plugins

import com.mock.application.routes.authRoute
import com.mock.application.routes.noteRoute
import com.mock.application.routes.userRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("MockServer")
        }
        authRoute()
        authenticate("auth-jwt") {
            noteRoute()
            userRoute()
        }
    }
}
