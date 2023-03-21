package com.mock.routes

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.mock.models.User
import com.mock.services.userService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.util.*

fun Route.auth(config: ApplicationConfig) {
    val secret = config.property("jwt.secret").getString()
    val expiration: Int = config.property("jwt.expiration").getString().toInt()

    post("/login") {
        val user = call.receive<User>()
        try {
            val user = userService.loginUser(user.username, user.password)
            user?.let {
                val token = JWT.create()
                    .withClaim("username", user.username)
                    .withClaim("userId", user.id)
                    .withExpiresAt(Date(System.currentTimeMillis() + expiration))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(hashMapOf("access_token" to token))
            } ?: kotlin.run {call.respond(HttpStatusCode.Unauthorized, "username/password is wrong") }
        }catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.Unauthorized, "Cannot find user")
        }

    }


    post("/register") {
        val user = call.receive<User>()
        val newUser = userService.createUser(user)
        newUser?.let {
            val token = JWT.create()
                .withClaim("username", user.username)
                .withClaim("userId", user.id)
                .withExpiresAt(Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("access_token" to token))
        }
        call.respond(HttpStatusCode.BadRequest)
    }
}