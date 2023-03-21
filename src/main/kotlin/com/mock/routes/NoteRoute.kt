package com.mock.routes

import com.mock.models.Note
import com.mock.models.User
import com.mock.services.noteService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.note(config: ApplicationConfig) {

    route("note") {
        get("/title") {
            val title = call.parameters["title"] ?: return@get call.respondText(
                "Missing title",
                status = HttpStatusCode.BadRequest
            )
            val principal = call.principal<JWTPrincipal>()
            principal?.let {
                val user = createUserSessionFromPrincipal(principal)
                val notes = noteService.findByTitle(title, user) ?: return@get call.respondText(
                    "No Note with title $title",
                    status = HttpStatusCode.NotFound
                )
                call.respond(notes)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        get("/all") {
            val principal = call.principal<JWTPrincipal>()
            principal?.let {
                val user = createUserSessionFromPrincipal(principal)
                val notes = noteService.findAll(user) ?: return@get call.respondText(
                    "Empty Notes",
                    status = HttpStatusCode.NotFound
                )
                call.respond(notes)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/create") {
            val note = call.receive<Note>()
            val principal = call.principal<JWTPrincipal>()
            principal?.let {
                val user = createUserSessionFromPrincipal(principal)
                val newNote = noteService.create(note, user) ?: return@post call.respondText(
                    "Create Note fail ${note}",
                    status = HttpStatusCode.NotFound
                )
                call.respond(newNote)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

fun createUserSessionFromPrincipal(jwtPrincipal: JWTPrincipal): User{
    val username = jwtPrincipal!!.payload.getClaim("username").asString()
    val userId = jwtPrincipal!!.payload.getClaim("userId").asInt()
    return User(id = userId,username = username)
}