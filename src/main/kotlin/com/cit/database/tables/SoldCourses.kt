package com.cit.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

object SoldCourses: Table() {
    val id = integer("id").autoIncrement()
    val idCourse = integer("idCourse")
    val startEducation = date("startEducation")
    val countLessonOfWeek = integer("countLessonOfWeek")
    val formatLessonsId = integer("formatLessonsId")
    val typeLessonsId = integer("typeLessonsId")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class SoldCourse(
    val id: Int,
    val idCourse: Int,
    val startEducation: LocalDate,
    val countLessonOfWeek: Int,
    val formatLessonsId: Int,
    val typeLessonsId: Int
)