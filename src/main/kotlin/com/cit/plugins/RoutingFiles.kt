package com.cit.plugins

import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.utils.*
import com.cit.utils.respond.respondAudio
import com.cit.utils.respond.respondImage
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureFilesRouting(){
    routing {
        get("image/{filename}"){
            val filename = call.receivePathParameter("filename") ?: return@get
            call.respondImage(filename)
        }

        post("media"){
            val filenameHeader = call.receiveHeaderParameter("filename")
            val filename = call.receiveTransformPrimitive<String>()?.replace("\n", "")?.replace("\r", "") ?: filenameHeader
            if (filename == null) {
                call.respondAnswer("Please check body or header: filename".asError<Unit>())
                return@post
            }
            call.respondImage(filename)
        }

        get("media/{audio}"){
            val audio = call.receivePathParameter("audio") ?: return@get
            call.respondAudio(audio)
        }

        // на swagger не документировать
        post("convertImage"){
            val image = call.receiveByteArray() ?: return@post
            call.respondAnswer(image.contentToString().asAnswer())
        }
    }
}