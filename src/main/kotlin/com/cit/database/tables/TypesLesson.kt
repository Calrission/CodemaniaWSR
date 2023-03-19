package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

object TypesLesson: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class TypeLesson(
    val id: Int,
    val name: String
)