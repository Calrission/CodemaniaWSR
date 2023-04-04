package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object TagTypes: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class TagTypeBody(
    val name: String
)

@Serializable
data class ModelTagType(
    val id: Int,
    val name: String
)