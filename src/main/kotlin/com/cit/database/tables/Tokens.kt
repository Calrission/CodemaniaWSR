package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

class Token(
    val id: Int,
    token: String,
    idUser: Int
): TokenBody(token, idUser)

open class TokenBody(
    token: String,
    val idUser: Int
): ModelToken(token)

@Serializable
open class ModelToken(
    val token: String
)

object Tokens: Table() {
    val id = integer("id").autoIncrement()
    val token = varchar("token", 100)
    val idUser = integer("idUser")

    override val primaryKey = PrimaryKey(id)
}