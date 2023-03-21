package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Roles: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class RoleBody(
    val name: String
)

@Serializable
data class ModelRole(
    val id: Int,
    val name: String
)