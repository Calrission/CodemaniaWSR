package com.cit.models.bodies

import com.cit.interfaces.ResultValidation
import com.cit.interfaces.Validation
import com.cit.utils.ValidationUtils.Companion.isValidEmail

abstract class IdentityBody: Validation{
    abstract val email: String
    abstract val password: String

    override fun validate(): ResultValidation{
        if (email.isEmpty())
            return ResultValidation(false, "Почта не должна быть пустой")
        if (!email.isValidEmail())
            return ResultValidation(false, "Почта не по поттерну")
        if (password.isEmpty())
            return ResultValidation(false, "Пароль не должен быть пустым")
        return ResultValidation(true)
    }
}