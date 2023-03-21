package com.cit.utils

import com.cit.database.dao.DAOToken
import com.cit.database.tables.User
import com.cit.interfaces.Validation
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

suspend inline fun ApplicationCall.receiveQueryToken(respondError: Boolean = true): String?{
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

suspend inline fun ApplicationCall.receiveUserFromToken(): User?{
    val token = receiveQueryToken(false) ?: return null
    return usersController.getUserByToken(token)
}

suspend inline fun ApplicationCall.receiveExistQueryToken(): Boolean{
    val token = receiveQueryToken(false)
    return token != null
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