package com.cit.database.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDate
import java.time.LocalDateTime
@Serializable
data class ModelHuman(
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val avatar: String,
    val type: ModelRole,
    val id: Int
)

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val avatar: String,
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
    val avatar = varchar("avatar", 100)
    val dateBirthDay = date("dateBirthDay")
    val dateTimeCreate = datetime("dateTimeCreate").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}