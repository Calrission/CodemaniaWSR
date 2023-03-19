package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object TypesChat: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

@Serializable
data class TypeChat(
    val id: Int,
    val name: String
)