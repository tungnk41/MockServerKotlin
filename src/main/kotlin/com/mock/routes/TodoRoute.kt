package com.mock.routes

import com.mock.auth.Session
import com.mock.repository.TodoRepository
import com.mock.repository.data.Todo
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions

const val TODOS = "todos"
const val FIND_TODO = "$TODOS"
const val CREATE_TODO = "$TODOS/create"
const val UPDATE_TODO = "$TODOS/update"
const val DELETE_TODO = "$TODOS/delete"

@Location(TODOS)
class TodoRoute

@Location(FIND_TODO)
class FindTodoRoute

@Location(CREATE_TODO)
class CreateTodoRoute

@Location(UPDATE_TODO)
class UpdateTodoRoute

@Location(DELETE_TODO)
class DeleteTodoRoute

fun Route.TodoRoute(repository: TodoRepository) {

    authenticate("jwt") {
        post<CreateTodoRoute> {
            val todosParameters = call.receive<Parameters>()
            val title =
                todosParameters["title"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Title")
            val content =
                todosParameters["content"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Content")
            val done =
                !(todosParameters["done"] == null ||  todosParameters["done"]!!.lowercase().equals("false"))

            val userId = call.sessions.get<Session>()?.userId ?: -1

            try {
                val currentTodo = repository.insert(userId = userId, todo = Todo(id = null,title = title, content = content, done = done))
                currentTodo?.id?.let {
                    call.respond(HttpStatusCode.OK, currentTodo)
                }
            } catch (e: Throwable) {
                application.log.error("Failed to add todo", e)
                call.respond(HttpStatusCode.BadRequest, "Problems Saving Todo")
            }
        }

        get<FindTodoRoute> {
            val userId = call.sessions.get<Session>()?.userId ?: -1

            val todosParameters = call.request.queryParameters
            val limit = if (todosParameters.contains("limit")) todosParameters["limit"] else null
            val offset = if (todosParameters.contains("offset")) todosParameters["offset"] else null
            val title = if (todosParameters.contains("title")) todosParameters["title"] else null

            try {
                if (limit != null && offset != null) {
//                    val todos = repository.getTodos(user.userId, offset.toInt(), limit.toInt())
//                    call.respond(todos)
                }
                else if (title != null) {
                    val todo = repository.findByTitle(userId = userId,title = title)
                    todo?.let {
                        call.respond(HttpStatusCode.OK,todo)
                    }
                }
                else {
                    val todos = repository.find(userId = userId)
                    todos?.let {
                        call.respond(HttpStatusCode.OK,todos)
                    }
                }
            } catch (e: Throwable) {
                application.log.error("Failed to get Todos", e)
                call.respond(HttpStatusCode.BadRequest, "Problems getting Todos")
            }
        }

        delete<DeleteTodoRoute> {
            val todosParameters = call.receive<Parameters>()
            if (!todosParameters.contains("id")) {
                return@delete call.respond(HttpStatusCode.BadRequest, "Missing Todo Id")
            }
            val todoId =
                todosParameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing Todo Id")
            val userId = call.sessions.get<Session>()?.userId ?: -1

            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@delete
            }

            try {
                repository.deleteByID(userId, todoId.toLong())
                call.respond(HttpStatusCode.OK)
            } catch (e: Throwable) {
                application.log.error("Failed to delete todo", e)
                call.respond(HttpStatusCode.BadRequest, "Problems Deleting Todo")
            }
        }
    }
}