package com.mock.application.routes

import com.mock.application.controller.AuthController
import com.mock.data.model.base.WrapDataResponse
import com.mock.data.model.request.LoginRequest
import com.mock.data.model.request.RefreshTokenRequest
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
            val response = authController.login(request)
            call.respond(response)
        }


        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = authController.register(request)
            call.respond(response)
        }

        post("/logout") {
            call.respond(HttpStatusCode.OK)
        }

        post("/refresh-token") {
            val request = call.receive<RefreshTokenRequest>()
            val response = authController.refreshToken(request)
            response?.let {
                call.respond(response)
            } ?: kotlin.run { call.respond(HttpStatusCode.Unauthorized) }
        }
    }
}