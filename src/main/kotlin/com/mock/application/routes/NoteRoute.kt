package com.mock.application.routes

import com.mock.application.auth.principal.UserPrincipal
import com.mock.application.controller.NoteController
import com.mock.data.database.entity.Note
import com.mock.data.model.base.WrapDataResponse
import com.mock.data.model.base.WrapListDataResponse
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
                val user = principal.user
                val notes = noteController.findByTitle(title, user) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(WrapListDataResponse(data = notes))
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        get("/id/{id?}") {
            val id = call.request.queryParameters.getOrFail<Int>("id")
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val note = noteController.findById(id, user) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(WrapDataResponse(data = note))
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        delete("/id/{id?}") {
            val id = call.request.queryParameters.getOrFail<Int>("id")
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val response = noteController.deleteById(id, user)
                call.respond(if (response) HttpStatusCode.OK else HttpStatusCode.NotFound)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        get("/all") {
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val notes = noteController.findAll(user) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(WrapListDataResponse(data = notes))
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/create") {
            val request = call.receive<NoteRequest>()
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val note = noteController.create(request, user) ?: return@post call.respond(HttpStatusCode.NotFound)
                call.respond(WrapDataResponse(data = note))
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        put("/edit") {
            val note = call.receive<NoteRequest>()
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val user = principal.user
                val response = noteController.update(note, user)
                call.respond(if (response) HttpStatusCode.OK else HttpStatusCode.NotFound)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}