package com.mock.server.routes

import com.mock.server.application.auth.principal.UserPrincipal
import com.mock.server.application.controller.NoteController
import com.mock.server.application.data.model.request.NoteRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.noteRoute() {

    val noteController by inject<NoteController>()

    route("/note") {

        get {
            val id = call.parameters["id"]?.toInt() ?: return@get
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.findById(id, userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond("Principal empty")
            }
        }
        delete {
            val id = call.parameters["id"]?.toInt() ?: return@delete
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.deleteById(id, userId)
                call.respond("Delete note is success")
            } ?: kotlin.run {
                call.respond("Principal empty")
            }
        }
        get("/all") {
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.findAll(userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond("Principal empty")
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
                call.respond("Principal empty")
            }
        }
        put("/edit") {
            val note = call.receive<NoteRequest>()
            val principal = call.principal<UserPrincipal>()
            principal?.let {
                val userId = principal.userId
                val response = noteController.update(note, userId)
                call.respond(response)
            } ?: kotlin.run {
                call.respond("Principal empty")
            }
        }
    }
}