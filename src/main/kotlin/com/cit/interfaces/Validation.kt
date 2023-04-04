package com.cit.interfaces

interface Validation {
    fun validate(): ResultValidation
}

data class ResultValidation(
    val success: Boolean,
    val error: String? = null
){
    companion object {
        fun String.isBadResultValidation(): ResultValidation = ResultValidation(false, this)

        fun goodValidation(): ResultValidation = ResultValidation(true)
    }
}