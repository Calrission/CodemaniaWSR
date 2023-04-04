package com.cit.utils

import com.cit.database.tables.User
import com.cit.models.Connection
import com.cit.models.MessageChat
import com.cit.models.ModelSystemMessage.Companion.isModelSystemMessage
import com.cit.utils.respond.sendSerializedModel
import io.ktor.server.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet

class WebSocketChatController {
    private val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    fun newConnectionUser(session: WebSocketServerSession, user: User): Connection{
        val newConnection = Connection(session, user)
        connections.add(newConnection)
        return newConnection
    }

    fun removeConnection(connection: Connection){
        connections.remove(connection)
    }

    fun getCountOnline(): Int = connections.size

    suspend fun sendSystemMessage(message: String){
        val modelSystemMessage = message.isModelSystemMessage()
        connections.forEach {
            it.session.sendSerialized(modelSystemMessage)
        }
    }

    suspend fun sendNewUserMessage(messageChat: MessageChat){
        connections.forEach {
            messageChat.isYou = it.user.id == messageChat.idUser
            it.session.sendSerializedModel(messageChat)
        }
    }
}