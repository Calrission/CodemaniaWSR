package com.cit.database.controllers

import com.cit.database.dao.DAOCourses
import com.cit.database.dao.DAOSoldCourses
import com.cit.database.tables.*
import com.cit.lessonsController
import com.cit.tagsController
import com.cit.usersController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.exposed.sql.and
import java.time.LocalDate

class CoursesController {
    private val daoCourses = DAOCourses()
    private val daoSoldCourse = DAOSoldCourses()

    suspend fun getAllCourses(): List<ModelCourse> = daoCourses.selectAll().map { it.toModelCourse() }

    suspend fun getUserCourses(idUser: Int): List<ModelCourse> =
        daoSoldCourse.selectMany { SoldCourses.idUser eq idUser }.mapNotNull { getModelCourse(it.idCourse) }

    suspend fun getModelCourse(idCourse: Int): ModelCourse? = daoCourses.selectSingle { Courses.id eq idCourse }?.toModelCourse()

    suspend fun getCourse(idCourse: Int): Course? = daoCourses.selectSingle { Courses.id eq idCourse }

    suspend fun getSoldCourse(idCourse: Int, idUser: Int): SoldModelCourse? = daoCourses.selectSingle { Courses.id eq idCourse }?.toSoldModelCourse(idUser)

    suspend fun newSoldCourse(idCourse: Int, idUser: Int): Boolean = daoSoldCourse.insert(SoldCourseBody(idCourse, idUser, LocalDate.now())) != null

    private suspend fun Course.toModelCourse(): ModelCourse{
        val tags = tagsController.getTagsCourse(id).map { it.id }
        val mentors = usersController.getMentorsCourse(id)
        val plan: List<SafeItemPlan> = parsePlan().map { it.toSafe() }
        return toModelCourse(tags, mentors, plan)
    }

    private suspend fun Course.toSoldModelCourse(idUser: Int): SoldModelCourse{
        val tags = tagsController.getTagsCourse(id).map { it.id }
        val mentors = usersController.getMentorsCourse(id)
        val plan: List<ModelLesson> = lessonsController.getLessonsCourseUser(id, idUser).map { it.toModelLesson() }
        return toSoldModelCourse(tags, mentors, plan)
    }

    suspend fun checkBuyCourseUser(idUser: Int, idCourse: Int): Boolean{
        return daoSoldCourse.selectSingle { SoldCourses.idUser.eq(idUser).and(SoldCourses.idCourse eq idCourse) } != null
    }

}