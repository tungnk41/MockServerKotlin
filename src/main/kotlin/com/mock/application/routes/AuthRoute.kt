package com.mock.application.routes

import com.mock.application.controller.AuthController
import com.mock.data.model.request.LoginRequest
import com.mock.data.model.request.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoute() {

    val authController by inject<AuthController>()

    route("/auth") {

        post("/login") {
            val request = call.receive<LoginRequest>()
            val loginResponse = authController.login(request)
            loginResponse?.let {
                call.respond(loginResponse)
            } ?: kotlin.run { call.respond(HttpStatusCode.BadRequest) }
        }


        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = authController.register(request)
            response?.let {
                call.respond(response)
            }
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}