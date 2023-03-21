package com.mock.routes

import com.mock.auth.JwtService
import com.mock.auth.Session
import com.mock.repository.UserRepository
import com.mock.repository.data.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val LOGIN = "/login"
const val LOGOUT = "/logout"
const val REGISTER = "/register"
const val DELETE = "/delete"

@Location(LOGIN)
class LoginRoute

@Location(LOGOUT)
class LogoutRoute

@Location(REGISTER)
class RegisterRoute

@Location(DELETE)
class DeleteRoute

fun Route.UserRoute(repository: UserRepository, jwtService: JwtService, hashFunction: (String) -> String) {

    post<LoginRoute> {
        val signinParameters = call.receive<Parameters>()
        val password = signinParameters["password"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val username = signinParameters["username"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")

        val passwordEncrypt = hashFunction(password)

        try {
            val currentUser = repository.findByName(username)
            currentUser?.id?.let {
                if (currentUser.password == passwordEncrypt) {
                    call.sessions.set(Session(it))
                    call.respondText(jwtService.generateToken(currentUser))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                }
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
        }
    }

    post<LogoutRoute> {
        val signinParameters = call.receive<Parameters>()
        val username = signinParameters["username"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")

        try {
            val currentUser = repository.findByName(username)
            currentUser?.id?.let {
                call.sessions.clear(call.sessions.findName(Session::class))
                call.respond(HttpStatusCode.OK)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
        }
    }

    post<RegisterRoute> {
        val signupParameters = call.receive<Parameters>()
        val password = signupParameters["password"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val username = signupParameters["username"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")

        val passwordEncrypt = hashFunction(password)

        try {
            val user = repository.insert(User(id = null, username = username, password = passwordEncrypt))

            user?.id?.let {
                call.sessions.set(Session(it))
                call.respond(HttpStatusCode.Created,"Register success")
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }
}