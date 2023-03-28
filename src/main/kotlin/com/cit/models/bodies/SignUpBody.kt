package com.cit.models.bodies

import com.cit.database.tables.InsertUserBody
import com.cit.enums.Sex
import com.cit.enums.Sex.Companion.isSex
import com.cit.interfaces.ResultValidation
import com.cit.utils.DateTimeUtils.Companion.isValidDate
import com.cit.utils.DateTimeUtils.Companion.parseDate
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SignUpBody(
    override val password: String,
    override val email: String,

    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val sex: String,
    val dateBirthDay: String,
): IdentityBody(){
    fun toUserBody(): InsertUserBody = InsertUserBody(
        email, password, firstname, lastname, patronymic,
        null, sex.isSex()!!.valueInt, dateBirthDay.parseDate()!!
    )

    override fun validate(): ResultValidation {
        val oldResult = super.validate()
        if (!oldResult.success)
            return oldResult

        if (firstname.isEmpty())
            return ResultValidation(false, "Имя не должно быть пустое")
        if (lastname.isEmpty())
            return ResultValidation(false, "Фамилия не должна быть пустой")
        if (patronymic.isEmpty())
            return ResultValidation(false, "Отчество не должно быть пустое")
        if (sex !in Sex.getAllValueStr())
            return ResultValidation(false, "Пол должен быть из ${Sex.getAllValueStr()}")
        if (!dateBirthDay.isValidDate())
            return ResultValidation(false, "Дата рождения не корректна")
        if (dateBirthDay.parseDate()!!.isAfter(LocalDate.now()))
            return ResultValidation(false, "Дата рождения не может быть в будущем")

        return ResultValidation(true)
    }
}