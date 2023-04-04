package com.cit.utils.respond

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable

@Serializable
data class ModelAnswerChat <T> (
    val type: String,
    val body: T
)

enum class TypeMessageChat(val value: String){
    MESSAGE("message"), CHAT("chat"), CHATS("chats"), AUDIO("audio"), PERSON("person")
}

suspend fun <T> WebSocketSession.sendSerializedModel(data: T, type: TypeMessageChat = TypeMessageChat.MESSAGE){
    val modelAnswerChat = ModelAnswerChat(type.value, data)
    send(Gson().toJson(modelAnswerChat))
}