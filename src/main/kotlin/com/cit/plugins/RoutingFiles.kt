package com.cit.plugins

import com.cit.usersController
import com.cit.utils.DateTimeUtils.Companion.getDateTimeFilename
import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import com.cit.utils.receivePathParameter
import com.cit.utils.receiveQueryParameter
import com.cit.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureFilesRouting(){

    val imageDirectory = getLocalProperty("images_directory")

    routing {
        post("profile/avatar"){
            val idUser = call.receiveQueryParameter("idUser")?.toInt() ?: return@post

            val format = call.receiveQueryParameter("format")
            val filename = getDateTimeFilename() + ".$format"

            val binaryData = call.receive<ByteArray>()
            if (binaryData.isEmpty()){
                call.respondError(error = "File not upload, please check existing")
                return@post
            }
            File("$imageDirectory/$filename").writeBytes(binaryData)
            val result = usersController.updateAvatar(filename, idUser)
            call.respond(HttpStatusCode.OK, result)
        }

        get("img/{filename}"){
            val filename = call.receivePathParameter("filename") ?: return@get
            val file = File("$imageDirectory/$filename")
            if (file.exists())
                call.respondFile(file)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}