package com.cit.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

class User(
    val id: Int,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val sex: Int,
    val dateBirthDay: LocalDate,
)

object Users: Table(){
    val id = integer("id").autoIncrement()
    val email = varchar("email", 100)
    val password = varchar("password", 100)
    val firstname = varchar("firstname", 100)
    val lastname = varchar("lastname", 100)
    val patronymic = varchar("patronymic", 100)
    val sex = integer("sex")
    val dateBirthDay = date("dateBirthDay")

    override val primaryKey = PrimaryKey(id)
}