package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Courses: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 1000)
    val description = varchar("description", 5000)
    val cover = varchar("cover", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class CourseBody(
    val title: String,
    val description: String,
    val cover: String
)

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val cover: String
){
    fun toModelCourse(tags: List<Tag>, mentors: List<ModelHuman>) = ModelCourse(id, title, description, tags, mentors, cover)
}

@Serializable
data class ModelCourse(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<Tag>,
    val mentors: List<ModelHuman>,
    val cover: String
){
    fun toModelCourseShort() = ModelCourseShort(id, title, tags, cover)
}

@Serializable
data class ModelCourseShort(
    val id: Int,
    val title: String,
    val tags: List<Tag>,
    val cover: String
)