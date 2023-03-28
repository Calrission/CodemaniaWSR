package com.cit.plugins

import com.cit.database.tables.ReceivePatchUserBody
import com.cit.profileController
import com.cit.utils.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureProfile(){
    routing {
        get("profile"){
            val idUser = call.receiveQueryParameter("idUser")?.toInt() ?: return@get
            call.respondAnswer(profileController.respondProfile(idUser))
        }

        patch("profile"){
            val user = call.receiveUserByQueryToken() ?: return@patch
            val body = call.receive<ReceivePatchUserBody>().toPatchUserBody()
            call.respondAnswer(profileController.respondPatchProfile(user.id, body))
        }

        get("profile/courses"){
            val idUser = call.receiveQueryParameter("idUser")?.toInt() ?: return@get
            call.respondAnswer(profileController.respondProfileCourses(idUser))
        }

        get("profile/tags"){
            val idUser = call.receiveQueryParameter("idUser")?.toInt() ?: return@get
            call.respondAnswer(profileController.respondProfileTags(idUser))
        }

        post("profile/avatar"){
            val idUser = call.receiveUserByQueryToken()?.id ?: return@post
            val format = call.receiveQueryParameter("format") ?: return@post
            val binaryData = call.receiveByteArray() ?: return@post
            call.respondAnswer(profileController.uploadAvatarProfile(idUser, format, binaryData))
        }

        get("profile/course"){
            val idCourse = call.receiveQueryParameter("idCourse")?.toInt() ?: return@get
            val idUser = call.receiveQueryParameter("idUser")?.toInt() ?: return@get
            call.respondAnswer(profileController.respondProfileCourse(idCourse, idUser))
        }
    }
}