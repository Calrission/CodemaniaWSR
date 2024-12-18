package com.cit.utils

import com.cit.database.tables.User
import com.cit.interfaces.Validation
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.models.ModelError
import com.cit.usersController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondError(code: HttpStatusCode = HttpStatusCode.BadRequest, error: String){
    respond(code, ModelError(error))
}

suspend inline fun <reified T> ApplicationCall.respondErrorFields(){
    val fields = T::class.java.declaredFields.toList().map { it.name }.filter { it != "Companion" }
    val fieldsText = fields.joinToString(separator = ", "){ it }
    respondAnswer("Bad body, please check${if (fieldsText.isNotEmpty()) ": $fields" else ""}".asError<Unit>())
}

suspend inline fun <reified T : Any> ApplicationCall.receiveTransform(respondError: Boolean = true): T?{
    try {
        return receive()
    } catch (e: Exception) {
        if (respondError)
            respondErrorFields<T>()
    }
    return null
}

suspend inline fun ApplicationCall.receiveTransformPrimitive(respondError: Boolean = true): String?{
    try {
        return receive<String>().replace("\n", "").replace("\r", "").ifEmpty { null }
    } catch (e: Exception) {
        if (respondError)
            respondError(error = "Please check exist body")
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

suspend inline fun ApplicationCall.receiveHeaderParameter(header: String, respondError: Boolean = true): String? {
    val value = request.headers[header]
    if (value == null && respondError){
        respondError(error = "Check header: $header")
        return null
    }
    return value
}

suspend inline fun ApplicationCall.receiveUserByQueryToken(respondError: Boolean = true): User? {
    val token = receiveQueryToken(respondError) ?: return null
    val user = usersController.getUserByToken(token)
    if (user == null && respondError){
        respondError(error="token not valid")
        return null
    }
    return user
}

suspend inline fun ApplicationCall.receiveUserByHeaderTokenOrIdUser(respondError: Boolean = true): User? {
    val token = receiveHeaderParameter("Authorization", false)?.substringAfter("Bearer ")
    val idUser = receiveHeaderParameter("idUser", false)?.toInt()
    if (token != null){
        val user = usersController.getUserByToken(token)
        if (user == null && respondError){
            respondError(error="Token not valid")
            return null
        }
        return user
    }else if (idUser != null){
        val user = usersController.getUser(idUser)
        if (user == null && respondError){
            respondError(error="User not found with idUser='$idUser'")
            return null
        }
        return user
    }else{
        if (respondError)
            respondError(error = "Please check header: Authorization: Bearer or idUser")
        return null
    }
}

suspend inline fun ApplicationCall.receiveUserByHeaderToken(respondError: Boolean = true): User? {
    val token = receiveHeaderParameter("Authorization", respondError)?.substringAfter("Bearer ") ?: return null
    val user = usersController.getUserByToken(token)
    if (user == null && respondError){
        respondError(error="token not valid")
        return null
    }
    return user
}

suspend inline fun <reified T> ApplicationCall.respondAnswer(modelAnswer: ModelAnswer<T>){
    if (modelAnswer.isError) {
        respondError(modelAnswer.httpStatusCode, modelAnswer.messageError)
    }else{
        this@respondAnswer.respond(modelAnswer.httpStatusCode, modelAnswer.answer!!)
    }
}

suspend inline fun <reified T : Validation> ApplicationCall.receiveValidation(): T?{
    val obj = receiveTransform<T>(true) ?: return null
    val resultValidation = obj.validate()
    if (resultValidation.success){
        return obj
    }
    respondError(error = resultValidation.error ?: "Unknown error")
    return null
}

suspend inline fun ApplicationCall.receiveByteArray(
    respondError: Boolean = true
): ByteArray? {
    var binaryData: ByteArray? = receive()
    binaryData = if (binaryData!!.isEmpty()) null else binaryData
    if (binaryData == null && respondError){
        respondError(error = "File not upload, please check existing")
        return null
    }
    return binaryData
}

