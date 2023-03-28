package com.cit.utils

import com.cit.enums.DirectionDay
import com.cit.models.ModelAnswer
import com.cit.utils.DateTimeUtils.Companion.parseDateFormatter
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern.compile

class ValidationUtils {
    companion object {
        private val emailRegex = compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun String.isValidEmail(): Boolean{
            return emailRegex.matcher(this).matches()
        }

        fun localDateValidation(date: String, pattern: String): ModelAnswer<Unit>{
            val valid = date.parseDateFormatter(DateTimeFormatter.ofPattern(pattern)) != null
            return if (valid)
                ModelAnswer()
            else
                ModelAnswer(isError = true, messageError = "$date not valid date, please check by pattern: $pattern")
        }

        fun directionDayValidation(direction: String): ModelAnswer<Unit>{
            val allVariants = DirectionDay.getAllMessages()
            val valid = direction in allVariants
            return if(valid)
                ModelAnswer()
            else
                ModelAnswer(isError = true, messageError = "$direction - is not direction of day, please check exist in $allVariants")
        }
    }
}