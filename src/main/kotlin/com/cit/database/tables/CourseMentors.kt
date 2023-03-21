package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

object CourseMentors: Table() {
    val id = integer("id").autoIncrement()
    val idCourse = integer("idCourse")
    val idMentor = integer("idMentor")
}

data class CourseMentorBody(
    val idCourse: Int,
    val idMentor: Int
)

data class CourseMentor(
    val id: Int,
    val idCourse: Int,
    val idMentor: Int
)