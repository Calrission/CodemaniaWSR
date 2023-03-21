package com.cit.database.controllers

import com.cit.database.tables.ModelCourseShort
import com.cit.database.tables.Tag

class CatalogController {

    private val tagsController = TagsController()
    private val coursesController = CoursesController()

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
}