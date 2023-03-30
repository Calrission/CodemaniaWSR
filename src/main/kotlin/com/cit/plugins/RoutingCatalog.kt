package com.cit.plugins

import com.cit.catalogController
import com.cit.materialsController
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.utils.receiveQueryParameter
import com.cit.utils.receiveUserByHeaderToken
import com.cit.utils.receiveUserByQueryToken
import com.cit.utils.respondAnswer
import io.ktor.server.application.*
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
            val result = ModelAnswer(answer = materialsController.getAllMaterials())
            call.respondAnswer(result)
        }
    }
}