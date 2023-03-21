package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Tags: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 500)

    override val primaryKey = PrimaryKey(id)
}
@Serializable
data class Tag (
    val id: Int,
    val name: String,
)

data class TagBody(
    val name: String,
    val typeId: Int
)