package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Courses: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 1000)
    val description = varchar("description", 5000)
    val minCountLessonOfWeek = integer("minCountLessonOfWeek")
    val maxCountLessonOfWeek = integer("maxCountLessonOfWeek")
    val cover = varchar("cover", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val minCountLessonOfWeek: Int,
    val maxCountLessonOfWeek: Int,
    val cover: String
)

@Serializable
data class ModelCourse(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<ModelTagWithoutType>,
    val mentors: List<ModelHuman>,
    val minCountLessonOfWeek: Int,
    val maxCountLessonOfWeek: Int,
    val cover: String
)