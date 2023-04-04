package com.cit.plugins

import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.utils.receiveByteArray
import com.cit.utils.receivePathParameter
import com.cit.utils.receiveTransformPrimitive
import com.cit.utils.respond.respondImage
import com.cit.utils.respondAnswer
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureFilesRouting(){
    routing {
        get("image/{filename}"){
            val filename = call.receivePathParameter("filename") ?: return@get
            call.respondImage(filename)
        }

        post("media"){
            val filename = call.receiveTransformPrimitive<String>()?.replace("\n", "")?.replace("\r", "") ?: return@post
            call.respondImage(filename)
        }

        post("convertImage"){
            val image = call.receiveByteArray() ?: return@post
            call.respondAnswer(image.contentToString().asAnswer())
        }
    }
}