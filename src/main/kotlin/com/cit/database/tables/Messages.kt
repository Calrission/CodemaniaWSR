package com.cit.database.tables

import com.cit.models.MessageChat
import com.cit.utils.DateTimeUtils
import com.cit.utils.DateTimeUtils.Companion.dateTimeFormatter
import com.cit.utils.DateTimeUtils.Companion.parseDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Messages: Table() {
    val id = integer("id").autoIncrement()
    val idChat = integer("idChat")
    val text = varchar("text", 1000)
    val datetime = datetime("datetime")
    val idUser = integer("idUser")
    val isAudio = bool("isAudio")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class Message(
    val id: Int,
    val idChat: Int,
    val text: String,
    val datetime: LocalDateTime,
    val idUser: Int,
    val isAudio: Boolean
){
    fun toModelMessage(modelHuman: ModelHuman): ModelMessage = ModelMessage(
        id, idChat, text, datetime.format(DateTimeUtils.dateTimeFormatter),
        modelHuman, isAudio
    )

    fun toSafe(): SafeMessage = SafeMessage(
        id, idChat, text, datetime.format(DateTimeUtils.dateTimeFormatter), idUser, isAudio
    )

    fun toMessageChat(): MessageChat = MessageChat(
        id, text, idUser,
        datetime = datetime.format(dateTimeFormatter),
        isAudio = isAudio)
}
@Serializable
data class SafeMessage(
    val id: Int,
    val idChat: Int,
    val text: String,
    val datetime: String,
    val idUser: Int,
    val isAudio: Boolean
)

@Serializable
data class ReceiveInsertMessage(
    val text: String,
    val isAudio: Boolean,
    val idUser: Int,
    val idChat: Int,
    val datetime: String,
){
    fun toInsertMessage(): InsertMessage = InsertMessage(text, isAudio, idUser, idChat, datetime.parseDateTime() ?: LocalDateTime.now())
}

data class InsertMessage(
    val text: String,
    val isAudio: Boolean,
    val idUser: Int,
    val idChat: Int,
    val datetime: LocalDateTime,
)

@Serializable
data class ModelMessage(
    val id: Int,
    val idChat: Int,
    val text: String,
    val datetime: String,
    val user: ModelHuman,
    val isAudio: Boolean
){
    fun toInsertMessage(): InsertMessage = InsertMessage(
        text, isAudio, user.id, idChat, datetime.parseDateTime() ?: LocalDateTime.now()
    )
}