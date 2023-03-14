package com.cit.models.bodies

import com.cit.interfaces.ResultValidation
import com.cit.interfaces.Validation

abstract class IdentityBody: Validation{
    abstract val login: String
    abstract val password: String

    override fun validate(): ResultValidation{
        if (login.isEmpty())
            return ResultValidation(false, "Логин не должен быть пустым")
        if (password.isEmpty())
            return ResultValidation(false, "Пароль не должен быть пустым")
        return ResultValidation(true)
    }
}