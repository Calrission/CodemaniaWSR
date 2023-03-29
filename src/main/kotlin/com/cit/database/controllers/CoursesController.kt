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

class CoursesController {
    private val daoCourses = DAOCourses()
    private val daoSoldCourse = DAOSoldCourses()

    suspend fun getAllCourses(): List<ModelCourse> = daoCourses.selectAll().map { it.toModelCourse() }

    suspend fun getUserCourses(idUser: Int): List<ModelCourse> =
        daoSoldCourse.selectMany { SoldCourses.idUser eq idUser }.mapNotNull { getCourse(it.idCourse) }

    suspend fun getCourse(idCourse: Int, idUser: Int? = null): ModelCourse? = daoCourses.selectSingle { Courses.id eq idCourse }?.toModelCourse(idUser)

    private suspend fun Course.toModelCourse(idUser: Int? = null): ModelCourse{
        val tags = tagsController.getTagsCourse(id).map { it.id }
        val mentors = usersController.getMentorsCourse(id)
        val plan: List<LessonBase> = if (idUser == null){
            Gson().fromJson<List<ItemPlan>?>(plan, object: TypeToken<List<ItemPlan>>() {}.type).map { it.toSafe() }
        }else{
            lessonsController.getLessonsCourseUser(id, idUser)
        }
        return toModelCourse(tags, mentors, plan)
    }

    suspend fun checkBuyCourseUser(idUser: Int, idCourse: Int): Boolean{
        return daoSoldCourse.selectSingle { SoldCourses.idUser.eq(idUser).and(SoldCourses.idCourse eq idCourse) } != null
    }

}