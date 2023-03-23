package com.cit.database.tables

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
)