package com.cit.utils

import com.cit.database.tables.User
import com.cit.interfaces.Validation
import com.cit.models.ModelAnswer
import com.cit.models.ModelError
import com.cit.usersController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondError(code: HttpStatusCode = HttpStatusCode.BadRequest, error: String){
    respond(code, ModelError(error))
}

suspend inline fun <reified T> ApplicationCall.responseErrorValidationFields(){
    val fields = T::class.java.declaredFields.toList().map { it.name }.filter { it != "Companion" }
    respondError(error = "Bad body, please check: ${fields.joinToString(separator = ", "){ it }}")
}

suspend inline fun <reified T : Any> ApplicationCall.receiveTransform(): T?{
    try {
        return receive()
    } catch (e: Exception) {
        responseErrorValidationFields<T>()
    }
    return null
}

suspend inline fun ApplicationCall.receiveQueryToken(respondError: Boolean = true): String? {
    val token = request.queryParameters["token"]
    if (token == null) {
        if (respondError) respondError(HttpStatusCode.Forbidden, "Check query parameter 'token'")
        return null
    }
    if (!usersController.checkValidToken(token)){
        if (respondError) respondError(HttpStatusCode.Forbidden, "token not valid")
        return null
    }
    return token
}

suspend inline fun ApplicationCall.receiveQueryParameters(requiredParameters: List<String>): Map<String, String>? {
    val data = requiredParameters.associateWith { request.queryParameters[it] }
    val notExistParameters = requiredParameters.filter { data[it] == null }
    if (notExistParameters.isNotEmpty()) {
        respondError(error = "Please check query parameters: ${notExistParameters.joinToString(", ")}")
        return null
    }
    return requiredParameters.associateWith { data[it]!! }
}

suspend inline fun ApplicationCall.receiveQueryParameter(
    parameter: String,
    respondError: Boolean = true
): String? {
    val value = request.queryParameters[parameter]
    if (value == null && respondError) {
        respondError(error = "Please check query parameter: $parameter")
        return null
    }
    return value
}

suspend inline fun ApplicationCall.receiveQueryValidateParameter(
    parameter: String,
    respondError: Boolean = true,
    validation: (parameter: String) -> ModelAnswer<Unit>
): String? {
    val value = receiveQueryParameter(parameter, respondError) ?: return null
    val resultValidation = validation(value)
    if (resultValidation.isError && respondError) {
        respondError(error = resultValidation.messageError)
        return null
    }
    return value
}

suspend inline fun ApplicationCall.receivePathParameter(parameter: String): String? {
    val value = parameters[parameter]
    if (value == null) {
        respondError(error = "Please check path parameter: $parameter")
        return null
    }
    return value
}

suspend inline fun ApplicationCall.receiveHeaderParameter(header: String): String? {
    val value = request.headers["filename"]
    if (value == null){
        respondError(error = "Check header: filename")
        return null
    }
    return value
}

suspend inline fun ApplicationCall.receiveUserByQueryToken(respondError: Boolean = true): User? {
    val token = receiveQueryToken(respondError) ?: return null
    val user = usersController.getUserByToken(token)
    if (user == null){
        if (respondError) respondError(error="token not valid")
        return null
    }
    return user
}

suspend inline fun <reified T> ApplicationCall.respond(modelAnswer: ModelAnswer<T>){
    if (modelAnswer.isError) {
        respondError(modelAnswer.httpStatusCode, modelAnswer.messageError)
    }else{
        this@respond.respond(modelAnswer.httpStatusCode, modelAnswer.answer!!)
    }
}

suspend inline fun <reified T : Validation> ApplicationCall.receiveValidation(): T?{
    val obj = receiveTransform<T>() ?: return null
    val resultValidation = obj.validate()
    if (resultValidation.success){
        return obj
    }
    respondError(error = resultValidation.error ?: "Unknown error")
    return null
}

