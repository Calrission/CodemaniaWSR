package com.cit.database.controllers

import com.cit.*
import com.cit.database.tables.ModelCourse
import com.cit.database.tables.ModelCourseShort
import com.cit.database.tables.SafeItemPlan
import com.cit.database.tables.Tag
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import io.ktor.http.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Random

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
        val course = coursesController.getModelCourse(idCourse)
        return if (course == null)
            "Курс не найден".asError(HttpStatusCode.NotFound)
        else {
            course.plan = course.plan.map { it }
            ModelAnswer(answer = course)
        }
    }

    suspend fun respondNewOrder(idUser: Int, ids: List<Int>): ModelAnswer<List<Boolean>>{
        return ids.map {
            newOrderCourse(it, idUser)
        }.asAnswer()
    }

    private suspend fun newOrderCourse(idCourse: Int, idUser: Int): Boolean{
        val course = coursesController.getCourse(idCourse) ?: return false
        val idsMentors = usersController.getMentorsCourse(idCourse).map { it.id }
        val planItems = course.parsePlan()
        val dateTimes = calcDateTimesLesson(LocalDate.now(), planItems.size)
        val bodies = planItems.mapIndexed { pos, it ->
            it.toLessonBody(idCourse, idUser, dateTimes[pos])
        }
        val resultInsertSold = coursesController.newSoldCourse(idCourse, idUser)
        if (!resultInsertSold)
            return false
        val resultsCreateLessons = lessonsController.insertNewLessons(bodies)
        createChats(idUser, idsMentors)
        return resultsCreateLessons.all { it }
    }

    private suspend fun createChats(idUser: Int, idsMentors: List<Int>): List<Boolean> {
        return idsMentors.map { chatController.createNewChat(idUser, it) != null }
    }

    private fun calcDateTimesLesson(startDate: LocalDate, count: Int): List<LocalDateTime>{
        val step = (1..6).random().toLong()
        return (0 until count).map {
            startDate.plusDays(step * it).atTime(12, 0).plusHours(step)
        }
    }
}