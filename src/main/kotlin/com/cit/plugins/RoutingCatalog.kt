package com.cit.plugins

import com.cit.catalogController
import com.cit.utils.receiveUserFromToken
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCatalog(){
    routing{
        get("catalog/tags"){
            call.respond(catalogController.getCatalogTags())
        }

        get("catalog/courses"){
            val user = call.receiveUserFromToken()
            if (user != null){
                call.respond(catalogController.getCatalogUserCourses(user.id))
            }else{
                call.respond(catalogController.getCatalogAllCourses())
            }
        }
    }
}