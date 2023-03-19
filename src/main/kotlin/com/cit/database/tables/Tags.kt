package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Tags: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 500)
    val typeId = integer("typeId")

    override val primaryKey = PrimaryKey(id)
}

data class Tag (
    val id: Int,
    val name: String,
    val typeId: Int
)

@Serializable
data class ModelTag(
    val id: Int,
    val name: String,
    val type: ModelTagType
)