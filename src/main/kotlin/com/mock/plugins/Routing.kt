package com.mock.plugins


import com.google.api.client.http.HttpResponse
import com.mock.application.routes.authRoute
import com.mock.application.routes.noteRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("MockServer")
        }
        authenticate("auth-jwt") {
            noteRoute()
        }
        authRoute()
    }
}
