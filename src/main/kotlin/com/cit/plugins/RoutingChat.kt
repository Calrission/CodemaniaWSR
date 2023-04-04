package com.cit.plugins

import com.cit.chatController
import com.cit.models.Connection
import com.cit.models.InComingMessage
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.models.ModelSystemMessage.Companion.isModelErrorSystemMessage
import com.cit.models.ModelSystemMessage.Companion.isModelSystemMessage
import com.cit.usersController
import com.cit.utils.*
import com.cit.utils.respond.respondAudio
import com.cit.utils.respond.uploadFile
import com.cit.webSocketChatController
import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureChat() {
    routing {
        webSocket("/chat"){
            val token = call.request.queryParameters["token"] ?: return@webSocket
            val user = usersController.getUserByToken(token) ?: return@webSocket
            val connection = webSocketChatController.newConnectionUser(this, user)

            try {
                webSocketChatController.sendSystemMessage(
                    "Новый участник чата: ${connection.user.getFIO()}! Онлайн: ${webSocketChatController.getCountOnline()}"
                )
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText: String = frame.readText()
                    if (receivedText == "/chats" || "/chat" in receivedText){
                        if (receivedText == "/chats") {
                            val answer = chatController.respondUserChats(connection.user.id)
                            if (answer.isError) {
                                connection.session.send(answer.messageError)
                            } else {
                                connection.session.sendSerialized(answer.answer)
                            }
                        }else{
                            val idChat = try {
                                receivedText.substringAfter("/chat ").toInt()
                            }catch (e: Exception){
                                connection.session.send("Не указан idChat. Пример: '/chat 1'")
                                null
                            } ?: continue
                            val answer = chatController.respondChat(connection.user.id, idChat)
                            if (answer.isError) {
                                connection.session.send(answer.messageError)
                            } else {
                                connection.session.sendSerialized(answer.answer)
                            }
                        }
                        continue
                    }
                    val text = receivedText.replace("\r\n", "")
                    val inComingMessage = Gson().fromJson(text, InComingMessage::class.java)
                    val message = if (inComingMessage.idChat == null || (!inComingMessage.isAudio && inComingMessage.text.isNullOrBlank()))
                        null
                    else
                        chatController.newMessage(inComingMessage.text ?: "", inComingMessage.idChat, connection.user.id, LocalDateTime.now(), inComingMessage.isAudio)
                    if (message == null) {
                        connection.session.sendSerialized("Сообщение не отправленно".isModelErrorSystemMessage())
                        continue
                    }
                    webSocketChatController.sendNewUserMessage(message.toMessageChat())
                }
            }catch (e: Exception){
                println(e.localizedMessage)
            }finally {
                webSocketChatController.removeConnection(connection)
                webSocketChatController.sendSystemMessage(
                    "Участник ${user.getFIO()} покинул нас! Онлайн: ${webSocketChatController.getCountOnline()}"
                )
            }

        }

        get("chats"){
            val user = call.receiveUserByHeaderTokenOrIdUser() ?: return@get
            call.respondAnswer(chatController.respondUserChats(user.id))
        }

        get("chat"){
            val user = call.receiveUserByHeaderTokenOrIdUser() ?: return@get
            val idChat = call.receivePathParameter("idChat")?.toInt() ?: return@get
            call.respondAnswer(chatController.respondChat(user.id, idChat))
        }

        get("audio/{idMessage}"){
            val idMessage = call.receivePathParameter("idMessage")?.toInt() ?: return@get
            val answer = chatController.respondGetAudioFromMessage(idMessage)
            if (answer.isError)
                call.respondAnswer(answer)
            else
                call.respondAudio(answer.answer!!)
        }

        post("audio/{idMessage}"){
            val user = call.receiveUserByHeaderTokenOrIdUser() ?: return@post
            val idMessage = call.receivePathParameter("idMessage")?.toInt() ?: return@post
            val bytes = call.receiveByteArray() ?: return@post

            if(!chatController.checkAccessUserToMessage(user.id, idMessage)){
                call.respondAnswer("К данному сообщению вы не имеете доступа".asError<Unit>())
                return@post
            }
            if (!chatController.checkExistMessage(idMessage)) {
                call.respondAnswer("Данное сообщение не существует".asError<Unit>())
                return@post
            }
            if (!chatController.checkIsAudioMessage(idMessage)) {
                call.respondAnswer("Данное сообщение не является аудиосообщением".asError<Unit>())
                return@post
            }
            if (chatController.checkExistAudioFileForAudioMessage(idMessage)) {
                call.respondAnswer("Для данного аудиосообщения уже существует аудио файл".asError<Unit>())
                return@post
            }

            val filename = "$idMessage.mp3"
            val newFile = uploadFile(filename, bytes)
            if (newFile.exists())
                call.respondAnswer(filename.asAnswer())
            else
                call.respondAnswer("Аудио не сохранено".asError<Unit>())
        }
    }
}