package com.cit.models

import io.ktor.http.*

data class ModelAnswer <T>(
    val httpStatusCode: HttpStatusCode = HttpStatusCode.OK,
    val answer: T? = null,
    val isError: Boolean = false,
    val messageError: String = ""
){
    companion object {
        fun <T> instanceError(
            httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest,
            message: String
        ): ModelAnswer<T> = ModelAnswer(httpStatusCode, isError = true, messageError = message)

        fun <T> T.asAnswer(httpStatusCode: HttpStatusCode = HttpStatusCode.OK): ModelAnswer<T>{
            return ModelAnswer(httpStatusCode, answer = this)
        }

        fun <T> String.asError(httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest): ModelAnswer<T>{
            return ModelAnswer(httpStatusCode, isError = true, messageError = this)
        }

    }
}