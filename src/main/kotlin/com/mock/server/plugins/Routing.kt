package com.mock.server.plugins

import com.mock.client.Script
import com.mock.server.routes.authRoute
import com.mock.server.routes.noteRoute
import com.mock.server.routes.userRoute
import com.mock.server.routes.webSocketRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val script by inject<Script>()

    routing {
        get("/") {
            call.respondText("MockServer")
        }

        route("/api/v1") {
            authRoute()
            authenticate("api-auth-jwt") {
                noteRoute()
                userRoute()
            }
        }

        route("/socket") {
            authenticate("socket-auth-jwt") {
                webSocketRoute()
            }
        }
    }
}
