package com.cit.database.controllers

import com.cit.catalogController
import com.cit.coursesController
import com.cit.database.tables.ModelCourse
import com.cit.database.tables.ModelCourseShort
import com.cit.database.tables.Tag
import com.cit.models.ModelAnswer
import com.cit.tagsController
import io.ktor.http.*

class CatalogController {
    suspend fun getCatalogTags(): List<Tag>{
        return tagsController.getALlTags()
    }

    suspend fun getCatalogAllCourses(): List<ModelCourseShort>{
        return coursesController.getAllCourses().map { it.toModelCourseShort() }
    }

    suspend fun getCatalogUserCourses(idUser: Int): List<ModelCourseShort>{
        val userCourses = coursesController.getUserCourses(idUser).map { it.toModelCourseShort() }
        return getCatalogAllCourses().minus(userCourses.toSet())
    }

    suspend fun respondCatalogCourses(idUser: Int?): ModelAnswer<List<ModelCourseShort>>{
        return ModelAnswer(answer = if (idUser != null){
            catalogController.getCatalogUserCourses(idUser)
        }else{
            catalogController.getCatalogAllCourses()
        })
    }

    suspend fun respondCourse(idCourse: Int): ModelAnswer<ModelCourse> {
        val course = coursesController.getCourse(idCourse)
        return if (course == null)
            ModelAnswer.instanceError(HttpStatusCode.NotFound, "Курс не найден")
        else
            ModelAnswer(answer = course)
    }
}