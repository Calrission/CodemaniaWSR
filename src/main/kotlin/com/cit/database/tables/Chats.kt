package com.cit.database.tables

import com.cit.usersController
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Chats: Table() {
    val id = integer("id").autoIncrement()
    val firstIdUser = integer("firstIdUser")
    val secondIdUser = integer("secondIdUser")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

@Serializable
data class Chat(
    val id: Int,
    val firstIdUser: Int,
    val secondIdUser: Int
){
    suspend fun toModelChat(): ModelChat? {
        val first = usersController.getUser(firstIdUser)?.toModelHuman() ?: return null
        val second = usersController.getUser(secondIdUser)?.toModelHuman() ?: return null
        return ModelChat(id, first, second)
    }

    fun getUsersIds(): List<Int> = listOf(firstIdUser, secondIdUser)
}

data class InsertChat(
    val firstIdUser: Int,
    val secondIdUser: Int
)

@Serializable
data class ModelChat(
    val id: Int,
    val first: ModelHuman,
    val second: ModelHuman
){
    fun addMessages(messages: List<SafeMessage>): ChatWithMessages = ChatWithMessages(this, messages)

    fun getUsers(): List<ModelHuman> = listOf(first, second)
}
@Serializable
data class ChatWithMessages(
    val chat: ModelChat,
    val messages: List<SafeMessage>
)