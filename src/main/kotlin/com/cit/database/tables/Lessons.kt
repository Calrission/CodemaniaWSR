package com.cit.database.tables

import com.cit.utils.DateTimeUtils
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
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
    override val title: String,
    override val description: String,
    val datetime: LocalDateTime,
    override val duration: Int,
    val isComplete: Boolean,
    val file: String?,
    val commentFile: String?,
): LessonBase() {
    fun toModelLesson(): ModelLesson = ModelLesson(
        id, idUser, title, description,
        datetime.format(DateTimeUtils.dateTimeFormatter),
        duration, isComplete, file, commentFile
    )
}

@Serializable
sealed class LessonBase {
    abstract val title: String
    abstract val description: String
    abstract val duration: Int
}

@Serializable
data class ModelLesson(
    val id: Int,
    val idCourse: Int,
    override val title: String,
    override val description: String,
    val datetime: String,
    override val duration: Int,
    val isComplete: Boolean,
    val file: String?,
    val commentFile: String?,
): LessonBase()

data class LessonBody(
    val idCourse: Int,
    val idUser: Int,
    override val title: String,
    override val description: String,
    val datetime: LocalDateTime,
    override val duration: Int,
    val isComplete: Boolean,
    val file: String? = null,
    val commentFile: String? = null,
): LessonBase()