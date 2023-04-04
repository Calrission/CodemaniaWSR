package com.cit.plugins

import com.cit.utils.receivePathParameter
import com.cit.utils.receiveTransformPrimitive
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
            val filename = call.receiveTransformPrimitive<String>()?.replace("\n", "")?.replace("\r", "") ?: return@post
            call.respondImage(filename)
        }
    }
}