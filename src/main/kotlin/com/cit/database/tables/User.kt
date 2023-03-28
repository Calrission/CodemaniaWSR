package com.cit.database.tables

import com.cit.enums.Sex
import com.cit.enums.Sex.Companion.isSex
import com.cit.utils.DateTimeUtils
import com.cit.utils.DateTimeUtils.Companion.parseDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDate
import java.time.LocalDateTime
@Serializable
data class ModelHuman(
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val avatar: String?,
    val id: Int,
)

@Serializable
data class IdentityResponse(
    val user: ModelHuman,
    val token: String
)

@Serializable
data class ModelProfile(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val avatar: String?,
    val courses: List<ModelCourseShort>
)

data class InsertUserBody(
    val email: String,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val password: String,
    val avatar: String?,
    val sex: Int,
    val dateBirthDay: LocalDate,
)
@Serializable
data class ReceivePatchUserBody(
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val sex: String?,
    val dateBirthDay: String?
){
    fun toPatchUserBody(): PatchUserBody = PatchUserBody(
        email, firstname, lastname,
        patronymic, sex?.isSex(), dateBirthDay?.parseDate()
    )
}

data class PatchUserBody(
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val sex: Sex?,
    val dateBirthDay: LocalDate?
)

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val avatar: String?,
    val sex: Int,
    val dateBirthDay: LocalDate,
){
    fun toModelHuman(): ModelHuman = ModelHuman(firstname, lastname, patronymic, avatar, id)
    fun getFIO(): String = "$lastname ${firstname[0]}.${patronymic[0]}."

    fun toPersonData(): PersonData = PersonData(
        id, email, firstname, lastname, patronymic, avatar,
        sex.isSex()!!.valueStr, dateBirthDay.format(DateTimeUtils.dateFormatter)
    )
}

@Serializable
data class PersonData(
    val id: Int,
    val email: String,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val avatar: String?,
    val sex: String,
    val dateBirthDay: String,
)

object Users: Table(){
    val id = integer("id").autoIncrement()
    val email = varchar("email", 100)
    val password = varchar("password", 100)
    val firstname = varchar("firstname", 100)
    val lastname = varchar("lastname", 100)
    val patronymic = varchar("patronymic", 100)
    val sex = integer("sex")
    val avatar = varchar("avatar", 100).nullable()
    val dateBirthDay = date("dateBirthDay")
    val dateTimeCreate = datetime("dateTimeCreate").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}