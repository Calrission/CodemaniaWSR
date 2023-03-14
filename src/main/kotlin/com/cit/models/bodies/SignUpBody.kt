package com.cit.models.bodies

import com.cit.interfaces.ResultValidation
import com.cit.utils.ValidationUtils.Companion.isValidEmail
import kotlinx.serialization.Serializable

@Serializable
data class SignUpBody(
    override val login: String,
    override val password: String,
    val email: String
): IdentityBody(){
    override fun validate(): ResultValidation {
        val beforeResult = super.validate()
        if (beforeResult.success){
            if (email.isEmpty())
                return ResultValidation(false, "Почта не должна быть пустой")
            if (!email.isValidEmail())
                return ResultValidation(false, "Почта не соотвествует паттерну")
            return beforeResult
        }else{
            return beforeResult
        }
    }
}