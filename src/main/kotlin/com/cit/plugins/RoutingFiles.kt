package com.cit.plugins

import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import com.cit.utils.receivePathParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

val imageDirectory = getLocalProperty("images_directory")

fun Application.configureFilesRouting(){



    routing {


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