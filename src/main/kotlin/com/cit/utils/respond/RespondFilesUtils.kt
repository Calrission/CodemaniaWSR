package com.cit.utils.respond

import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.io.File

suspend fun ApplicationCall.respondImage(filename: String){
    respondFile(getLocalProperty("images_path"), filename)
}

suspend inline fun ApplicationCall.respondFile(pathFile: String, filename: String){
    response.header(
        HttpHeaders.ContentDisposition,
        ContentDisposition.Attachment.withParameter(
            ContentDisposition.Parameters.FileName, filename
        ).toString()
    )
    respondFile(File(pathFile + filename))
}
