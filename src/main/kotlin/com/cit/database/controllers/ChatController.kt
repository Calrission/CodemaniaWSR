package com.cit.database.controllers

import com.cit.chatController
import com.cit.database.dao.DAOChats
import com.cit.database.dao.DAOMessages
import com.cit.database.tables.*
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.utils.LocalPropertiesUtils
import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import java.io.File
import java.time.LocalDateTime

class ChatController {

    private val daoChats = DAOChats()
    private val daoMessages = DAOMessages()

    suspend fun respondUserChats(idUser: Int): ModelAnswer<List<ModelChat>>{
        val chats = getChatsUser(idUser).mapNotNull {
            it.toModelChat()
        }
        return chats.asAnswer()
    }

    suspend fun respondChat(idUser: Int, idChat: Int): ModelAnswer<ChatWithMessages> {
        val chat = getChat(idChat, idUser)?.toModelChat() ?: return "Чат не найден".asError(HttpStatusCode.NotFound)
        val messages = getAllMessagesChat(chat.id)
        return chat.addMessages(messages).asAnswer()
    }

    suspend fun respondAllMessagesChat(idUser: Int, idChat: Int): ModelAnswer<List<SafeMessage>> {
        val chat = getChat(idChat, idUser) ?: return "Чат не найден".asError(HttpStatusCode.NotFound)
        return getAllMessagesChat(chat.id).asAnswer()
    }

    suspend fun getChat(idChat: Int, idUser: Int): Chat? = daoChats.selectSingle {
        Chats.id.eq(idChat).and(Chats.firstIdUser.eq(idUser).or(Chats.secondIdUser.eq(idUser)))
    }

    suspend fun getChatsUser(idUser: Int): List<Chat> = daoChats.selectMany { Chats.firstIdUser.eq(idUser).or(Chats.secondIdUser.eq(idUser)) }

    suspend fun getAllMessagesChat(idChat: Int): List<SafeMessage> {
        return daoMessages.selectMany { Messages.idChat eq idChat }.map { it.toSafe() }
    }
    suspend fun respondGetAudioFromMessage(idMessage: Int): ModelAnswer<String> {
        val message = chatController.getMessage(idMessage) ?: return "Сообщение не найдено".asError(HttpStatusCode.NotFound)
        if (!message.isAudio)
            return "Данное сообщение не аудио".asError()
        return "$idMessage.mp3".asAnswer()
    }

    suspend fun getMessage(idMessage: Int): Message? = daoMessages.selectSingle { Messages.id eq idMessage }

    suspend fun newMessage(message: String, idChat: Int, idUser: Int, dateTime: LocalDateTime, isAudio: Boolean): Message? {
        val model = InsertMessage(message, isAudio, idUser, idChat, dateTime)
        return daoMessages.insert(model)
    }

    suspend fun checkAccessUserToMessage(idUser: Int, idMessage: Int): Boolean {
        return daoMessages.selectSingle { (Messages.idUser eq idUser).and(Messages.id eq idMessage) } != null
    }

    suspend fun checkExistMessage(idMessage: Int): Boolean = daoMessages.selectSingle { Messages.id eq idMessage } != null

    suspend fun checkIsAudioMessage(idMessage: Int): Boolean = daoMessages.selectSingle { (Messages.id eq idMessage).and(Messages.isAudio eq true) } != null

    fun checkExistAudioFileForAudioMessage(idMessage: Int): Boolean{
        val file = File(LocalPropertiesUtils.getLocalProperty("audio_path") + "/" + idMessage + ".mp3")
        return file.exists()
    }

    suspend fun createNewChat(idUser: Int, idMentor: Int): Chat? {
        if (!daoChats.checkExistChat(idUser, idMentor)){
            return daoChats.insert(InsertChat(idUser, idMentor))
        }
        return daoChats.getChat(idUser, idMentor)
    }
}