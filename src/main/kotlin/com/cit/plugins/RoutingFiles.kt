package com.cit.plugins

import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import com.cit.utils.receivePathParameter
import com.cit.utils.respond.respondImage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Application.configureFilesRouting(){
    routing {
        get("img/{filename}"){
            val filename = call.receivePathParameter("filename") ?: return@get
            call.respondImage(filename)
        }
    }
}