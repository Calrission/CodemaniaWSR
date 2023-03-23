package com.cit.plugins

import com.cit.models.Connection
import com.cit.models.ModelMessage
import com.cit.models.ModelSystemMessage
import com.cit.usersController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.lang.Exception
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureChatSockets() {
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat"){
            val token = call.request.queryParameters["token"] ?: return@webSocket
            val user = usersController.getUserByToken(token)?: return@webSocket
            val connection = Connection(this, user)
            connections += connection
            try {
                val systemMessage = ModelSystemMessage("Новый участник чата: ${connection.user.getFIO()}! Онлайн: ${connections.count()}")
                connections.forEach {
                    it.session.sendSerialized(systemMessage)
                }
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val modelMessage = ModelMessage(receivedText, connection.user.toModelHuman())
                    connections.forEach {
                        it.session.sendSerialized(modelMessage)
                    }
                }
            }catch (e: Exception){
                println(e.localizedMessage)
            }finally {
                connections -= connection
                val systemMessage = ModelSystemMessage("Участник ${user.getFIO()} покинул нас! Онлайн: ${connections.count()}")
                connections.forEach {
                    it.session.sendSerialized(systemMessage)
                }
            }
        }
    }
}