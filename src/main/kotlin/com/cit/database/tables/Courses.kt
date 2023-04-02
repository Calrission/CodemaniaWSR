package com.cit.database.tables

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.http.ContentType.*
import kotlinx.serialization.Serializable
import org.h2.table.PlanItem
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime


object Courses: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 1000)
    val description = varchar("description", 5000)
    val cover = varchar("cover", 100)
    val plan = varchar("plan", 50000)
    val price = integer("price")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class CourseBody(
    val title: String,
    val description: String,
    val cover: String,
    val plan: String,
    val price: Int
)

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val cover: String,
    val plan: String,
    val price: Int
){
    fun toModelCourse(tagsIds: List<Int>, mentors: List<ModelHuman>, plan: List<SafeItemPlan>) = ModelCourse(
        id, title, description, tagsIds,
        mentors, cover, plan, price
    )

    fun toSoldModelCourse(tagsIds: List<Int>, mentors: List<ModelHuman>, plan: List<ModelLesson>) = SoldModelCourse(
        id, title, description, tagsIds,
        mentors, cover, plan, price
    )

    fun parsePlan(): List<ItemPlan> = Gson().fromJson(plan, object: TypeToken<List<ItemPlan>>() {}.type)
}

@Serializable
data class ModelCourse(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<Int>,
    val mentors: List<ModelHuman>,
    val cover: String,
    var plan: List<SafeItemPlan>,
    val price: Int
){
    fun toModelCourseShort() = ModelCourseShort(id, title, tags, cover, price)
}

@Serializable
data class SoldModelCourse(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<Int>,
    val mentors: List<ModelHuman>,
    val cover: String,
    var plan: List<ModelLesson>,
    val price: Int
){
    fun toModelCourseShort() = ModelCourseShort(id, title, tags, cover, price)
}

@Serializable
data class ModelCourseShort(
    val id: Int,
    val title: String,
    val tags: List<Int>,
    val cover: String,
    val price: Int
)

@Serializable
data class ItemPlan(
    val title: String,
    val description: String,
    val duration: Int,
    val file: String?,
    val commentFile: String?
){
    fun toSafe(): SafeItemPlan = SafeItemPlan(title, description, duration)

    fun toLessonBody(idCourse: Int, idUser: Int, dateTime: LocalDateTime, isComplete: Boolean = false): LessonBody = LessonBody(
        idCourse, idUser, title, description, dateTime, duration, isComplete, file, commentFile
    )
}

@Serializable
data class SafeItemPlan(
    val title: String,
    val description: String,
    val duration: Int,
)