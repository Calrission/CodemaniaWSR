package com.cit.database.controllers

import com.cit.database.dao.DAOCourses
import com.cit.database.dao.DAOSoldCourses
import com.cit.database.tables.*
import com.cit.tagsController
import com.cit.usersController
import org.jetbrains.exposed.sql.and

class CoursesController {
    private val daoCourses = DAOCourses()
    private val daoSoldCourse = DAOSoldCourses()

    suspend fun getAllCourses(): List<ModelCourse> = daoCourses.selectAll().map { it.toModelCourse() }

    suspend fun getUserCourses(idUser: Int): List<ModelCourse> =
        daoSoldCourse.selectMany { SoldCourses.idUser eq idUser }.mapNotNull { getCourse(it.idCourse) }

    suspend fun getCourse(idCourse: Int): ModelCourse? = daoCourses.selectSingle { Courses.id eq idCourse }?.toModelCourse()

    private suspend fun Course.toModelCourse(): ModelCourse{
        val tags = tagsController.getTagsCourse(id).map { it.id }
        val mentors = usersController.getMentorsCourse(id)
        return toModelCourse(tags, mentors)
    }

    suspend fun checkBuyCourseUser(idUser: Int, idCourse: Int): Boolean{
        return daoSoldCourse.selectSingle { SoldCourses.idUser.eq(idUser).and(SoldCourses.idCourse eq idCourse) } != null
    }

}