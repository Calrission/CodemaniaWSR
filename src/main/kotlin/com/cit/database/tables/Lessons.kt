package com.cit.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object Lessons: Table() {
    val id = integer("id").autoIncrement()
    val idCourse = integer("idCourse")
    val idUser = integer("idUser")
    val title = varchar("title", 500)
    val description = varchar("description", 5000)
    val datetime = datetime("datetime")
    val duration = integer("duration")
    val isComplete = bool("isComplete")
    val file = varchar("file", 250).nullable()
    val commentFile = varchar("commentFile", 5000).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class Lesson(
    val id: Int,
    val idCourse: Int,
    val idUser: Int,
    val title: String,
    val description: String,
    val datetime: LocalDateTime,
    val duration: Int,
    val isComplete: Boolean,
    val file: String?,
    val commentFile: String?,
)

data class LessonBody(
    val idCourse: Int,
    val idUser: Int,
    val title: String,
    val description: String,
    val datetime: LocalDateTime,
    val duration: Int,
    val isComplete: Boolean,
    val file: String? = null,
    val commentFile: String? = null,
)