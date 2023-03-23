package com.mock.application.routes

import com.mock.application.auth.principal.UserPrincipal
import com.mock.application.controller.NoteController
import com.mock.data.model.request.NoteRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject

fun Route.noteRoute() {

    val noteController by inject<NoteController>()

    route("/note") {
        get("/title/{title?}") {
            val title = call.request.queryParameters.getOrFail<String>("title")
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.findByTitle(title, userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        get("/id/{id?}") {
            val id = call.request.queryParameters.getOrFail<Int>("id")
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.findById(id, userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        delete("/id/{id?}") {
            val id = call.request.queryParameters.getOrFail<Int>("id")
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.deleteById(id, userId)
                call.respond(if (response) HttpStatusCode.OK else HttpStatusCode.NotFound)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        get("/all") {
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.findAll(userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/create") {
            val request = call.receive<NoteRequest>()
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.create(request, userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        put("/edit") {
            val note = call.receive<NoteRequest>()
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.update(note, userId)
                call.respond(if (response) HttpStatusCode.OK else HttpStatusCode.NotFound)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}