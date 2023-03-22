package com.mock.application.routes

import com.mock.application.auth.principal.UserPrincipal
import com.mock.application.model.Note
import com.mock.application.controller.noteController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoute() {

    route("note") {
        get("/title") {
            val title = call.parameters["title"] ?: return@get call.respondText(
                "Missing title",
                status = HttpStatusCode.BadRequest
            )
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val notes = noteController.findByTitle(title, user) ?: return@get call.respondText(
                    "No Note with title $title",
                    status = HttpStatusCode.NotFound
                )
                call.respond(notes)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        get("/all") {
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val notes = noteController.findAll(user) ?: return@get call.respondText(
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
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val newNote = noteController.create(note, user) ?: return@post call.respondText(
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