package com.mock.routes


import com.mock.application.websocket.Connection
import io.ktor.websocket.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.util.*

fun Route.webSocketRoute() {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
                e.printStackTrace()
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
}