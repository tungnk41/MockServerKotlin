package com.mock.plugins

import com.mock.routes.authRoute
import com.mock.routes.noteRoute
import com.mock.routes.userRoute
import com.mock.routes.webSocketRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("MockServer")
        }

        route("/api/v1") {
            authRoute()
            authenticate("auth-jwt") {
                noteRoute()
                userRoute()
            }
        }
        route("/socket") {
            webSocketRoute()
        }
    }
}
