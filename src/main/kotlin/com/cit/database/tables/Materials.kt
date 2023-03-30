package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Materials: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 1500)
    val cover = varchar("cover", 500)
    val url = varchar("url", 500)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class MaterialBody(
    val title: String,
    val cover: String,
    val url: String
)
@Serializable
data class Material(
    val id: Int,
    val title: String,
    val cover: String,
    val url: String
)