package com.cit.utils.respond

import com.cit.models.ModelAnswer.Companion.asError
import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import com.cit.utils.respondAnswer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.io.File

suspend fun ApplicationCall.respondImage(filename: String){
    respondFile(getLocalProperty("images_path"), filename)
}

suspend fun ApplicationCall.respondAudio(filename: String){
    respondFile(getLocalProperty("audio_path"), filename)
}

suspend inline fun ApplicationCall.respondFile(pathFile: String, filename: String){
    val file = File("$pathFile/$filename")
    if (!file.exists())
        respondAnswer("Файл не найден".asError<Unit>())
    else {
        response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(
                ContentDisposition.Parameters.FileName, filename
            ).toString()
        )
        respondFile(file)
    }
}

fun uploadAudio(filename: String, byteArray: ByteArray): File{
    val file = File("${getLocalProperty("audio_path")}/${filename}")
    file.writeBytes(byteArray)
    return file
}


fun uploadImage(nameImage: String, byteArray: ByteArray): File{
    val file = File("${getLocalProperty("images_path")}/${nameImage}")
    file.writeBytes(byteArray)
    return file
}

