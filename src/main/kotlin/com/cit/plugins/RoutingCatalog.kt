package com.cit.plugins

import com.cit.catalogController
import com.cit.materialsController
import com.cit.models.ModelAnswer
import com.cit.utils.receiveQueryParameter
import com.cit.utils.receiveUserFromToken
import com.cit.utils.respondModelAnswer
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCatalog(){
    routing{
        get("catalog/tags"){
            call.respond(catalogController.getCatalogTags())
        }

        get("catalog/courses"){
            val idUser = call.receiveUserFromToken()?.id
            call.respondModelAnswer(catalogController.respondCatalogCourses(idUser))
        }

        get("catalog/course"){
            val idCourse = call.receiveQueryParameter("idCourse")?.toInt() ?: return@get
            call.respondModelAnswer(catalogController.respondCourse(idCourse))
        }

        get("catalog/materials"){
            val result = ModelAnswer(answer = materialsController.getAllMaterials())
            call.respondModelAnswer(result)
        }
    }
}