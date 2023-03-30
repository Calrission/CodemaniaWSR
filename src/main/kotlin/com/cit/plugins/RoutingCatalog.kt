package com.cit.plugins

import com.cit.catalogController
import com.cit.materialsController
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.utils.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCatalog(){
    routing{
        get("catalog/tags"){
            call.respondAnswer(catalogController.getCatalogTags().asAnswer())
        }

        get("catalog/courses"){
            val idUser = call.receiveUserByHeaderToken(respondError = false)?.id
            call.respondAnswer(catalogController.respondCatalogCourses(idUser))
        }

        get("catalog/course"){
            val idCourse = call.receiveQueryParameter("idCourse")?.toInt() ?: return@get
            val course = catalogController.respondCourse(idCourse)
            call.respondAnswer(course)
        }

        get("catalog/materials"){
            call.respondAnswer(materialsController.getAllMaterials().asAnswer())
        }

        post("catalog/orderCreate"){
            val user = call.receiveUserByHeaderToken() ?: return@post
            val ids = call.receiveTransform<List<Int>>() ?: return@post

        }
    }
}