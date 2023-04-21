package com.cit.database.tables

import com.cit.coursesController
import com.cit.lessonsController
import com.cit.tagsController
import com.cit.usersController
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object SoldCourses: Table() {
    val id = integer("id").autoIncrement()
    val idCourse = integer("idCourse")
    val idUser = integer("idUser")
    val startEducation = date("startEducation")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class SoldCourseBody(
    val idCourse: Int,
    val idUser: Int,
    val startEducation: LocalDate,
)

data class SoldCourse(
    val id: Int,
    val idCourse: Int,
    val idUser: Int,
    val startEducation: LocalDate,
){
    suspend fun toModelSoldCourse(): SoldModelCourse?{
        val course = coursesController.getCourse(idCourse)?: return null
        val tags = tagsController.getTagsCourse(idCourse).map { it.id }
        val mentors = usersController.getMentorsCourse(idCourse)
        val lessons = lessonsController.getLessonsCourseUser(idCourse, idUser).map { it.toModelLesson() }
        return SoldModelCourse(id, course.title, course.description, tags, mentors, course.cover, lessons, course.price)
    }
}