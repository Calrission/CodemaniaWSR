package com.cit.plugins

import com.cit.utils.receivePathParameter
import com.cit.utils.respond.respondImage
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureFilesRouting(){
    routing {
        get("img/{filename}"){
            val filename = call.receivePathParameter("filename") ?: return@get
            call.respondImage(filename)
        }
    }
}