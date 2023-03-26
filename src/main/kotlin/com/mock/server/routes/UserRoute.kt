package com.mock.server.routes

import com.mock.server.application.auth.principal.UserPrincipal
import com.mock.server.application.controller.UserController
import com.mock.server.application.data.model.request.RefreshTokenRequest
import com.mock.server.application.data.model.request.UserRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {

    val userController by inject<UserController>()

    route("/user") {
        get("/info") {
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = userController.findUserById(userId = userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond("Principal empty")
            }
        }

        post("/create") {
            val request = call.receive<UserRequest>()
            val response = userController.createUser(request.username,request.password)
            call.respond(response)
        }

        get("/logout") {
            call.respond(HttpStatusCode.OK)
        }

    }
}