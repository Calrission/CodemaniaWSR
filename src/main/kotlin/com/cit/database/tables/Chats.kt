package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Chats: Table() {
    val id = integer("id").autoIncrement()
    val firstIdUser = integer("firstIdUser")
    val secondIdUser = integer("secondIdUser")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class Chat(
    val id: Int,
    val firstIdUser: Int,
    val secondIdUser: Int
)

@Serializable
data class ModelChat(
    val id: Int,
    val first: ModelHuman,
    val second: ModelHuman
)