package com.mock.server.routes

import com.mock.client.model.TokenResponse
import com.mock.server.application.auth.principal.UserPrincipal
import com.mock.server.application.controller.AuthController
import com.mock.server.application.data.model.request.LoginRequest
import com.mock.server.application.data.model.request.RefreshTokenRequest
import com.mock.server.application.data.model.request.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
            response?.let {
                call.respond(response)
            } ?: kotlin.run { call.respond("The user does not exist") }
        }

        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = authController.register(request)
            call.respond(response)
        }

        authenticate("api-auth-jwt") {
            get("/refresh-token") {
                val principal = call.principal<UserPrincipal>()
                principal?.let {
                    val userId = principal.userId
                    val username = principal.username
                    val response = authController.refreshToken(userId,username)
                    response?.let {
                        call.respond(response)
                    } ?: kotlin.run { call.respond(HttpStatusCode.Unauthorized) }
                } ?: kotlin.run {
                    call.respond("Principal empty")
                }
            }
        }

    }
}