package com.cit.database.tables

import com.cit.enums.Sex
import com.cit.enums.Sex.Companion.isSex
import com.cit.interfaces.ResultValidation
import com.cit.interfaces.ResultValidation.Companion.goodValidation
import com.cit.interfaces.ResultValidation.Companion.isBadResultValidation
import com.cit.interfaces.Validation
import com.cit.utils.DateTimeUtils
import com.cit.utils.DateTimeUtils.Companion.parseDate
import com.cit.utils.ValidationUtils.Companion.isValidEmail
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
    val user: PersonData,
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
    val email: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val sex: String? = null,
    val dateBirthDay: String? = null,
    val avatar: String? = null,
    val format: String? = null
): Validation{

    fun getByteArrayAvatar(): ByteArray? {
        avatar ?: return null
        val avatarString = avatar.replace("[", "").replace("]", "")
        return avatarString.split(", ").map { it.toByte() }.toByteArray()
    }

    override fun validate(): ResultValidation {
        if (email != null){
            if (email.isBlank())
                return "Почта пустая".isBadResultValidation()
            if (!email.isValidEmail())
                return "Почта не соотвествует паттерну".isBadResultValidation()
        }
        if (firstname != null && firstname.isBlank())
            return "Имя пустое".isBadResultValidation()
        if (lastname != null && lastname.isBlank())
            return "Фамилия пустая".isBadResultValidation()
        if (patronymic != null && patronymic.isBlank())
            return "Отчество пустое".isBadResultValidation()
        if (sex != null && sex.isSex() == null)
            return "В качестве пола доступно только ${Sex.getAllValueStr()}".isBadResultValidation()
        if (dateBirthDay != null && dateBirthDay.parseDate() == null)
            return "Дата рождения должна быть по паттерну. Пример: 18.12.2001".isBadResultValidation()
        if (dateBirthDay != null && dateBirthDay.parseDate()!!.isAfter(LocalDate.now()))
            return "День рождение не может быть в будущем".isBadResultValidation()
        if (avatar != null && format == null)
            return "При передачи аватарки необходим format".isBadResultValidation()
        return goodValidation()
    }

    fun toPatchUserBody(avatar: String?): PatchUserBody = PatchUserBody(
        email, firstname, lastname,
        patronymic, sex?.isSex(), dateBirthDay?.parseDate(), avatar
    )
}

data class PatchUserBody(
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val sex: Sex?,
    val dateBirthDay: LocalDate?,
    val avatar: String?
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

    fun toIdentityResponse(token: String): IdentityResponse = IdentityResponse(
        toPersonData(), token
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