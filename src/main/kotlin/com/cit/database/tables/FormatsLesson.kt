package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

object FormatsLesson: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class FormatLessonBody(
    val name: String
)

data class FormatLesson(
    val id: Int,
    val name: String
)