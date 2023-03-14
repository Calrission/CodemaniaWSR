package com.cit.utils

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
    }
}