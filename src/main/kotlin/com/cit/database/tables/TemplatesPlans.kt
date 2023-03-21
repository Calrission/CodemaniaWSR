package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

object TemplatesPlans: Table() {
    val idCourse = integer("idCourse")
    val plan = varchar("plan", 50000)

    override val primaryKey: PrimaryKey = PrimaryKey(idCourse)
}

data class TemplatePlan(
    val idCourse: Int,
    val plan: String
)