package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

object TagsCourses: Table() {
    val id = integer("id").autoIncrement()
    val idCourse = integer("idCourse")
    val idTag = integer("idTag")
}

data class TagCourse(
    val id: Int,
    val idCourse: Int,
    val idTag: Int
)