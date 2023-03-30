package com.cit.database.controllers

import com.cit.database.tables.ModelLesson
import com.cit.enums.DirectionDay
import com.cit.lessonsController
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.models.ModelAnswer.Companion.asAnswer
import io.ktor.http.*
import java.time.LocalDate
import java.time.LocalDateTime

class WorkSpaceController {

    suspend fun respondPlanDate(
        date: LocalDate?,
        idUser: Int,
        directionDay: DirectionDay?,
        idCourse: Int?
    ): ModelAnswer<List<ModelLesson>>{
        var targetDate = date ?: lessonsController.getNearDateWithLessonsUser(idUser, LocalDateTime.now())
        targetDate = (if (directionDay == null) targetDate else (if (targetDate != null) directionDay.getDateDirectionByNow(idUser, targetDate) else null))
            ?: return "Для указаной даты/направления даты занятий нет".asError(HttpStatusCode.NotFound)
        val lessons = lessonsController.getLessonsUserByDate(idUser, targetDate)
            .filter { if (idCourse != null) it.idCourse == idCourse else true }
            .map { it.toModelLesson() }
        return lessons.asAnswer()
    }

    private suspend fun DirectionDay.getDateDirectionByNow(idUser: Int, dateTarget: LocalDate): LocalDate?{
        if (this == DirectionDay.NEXT)
            return lessonsController.getNextDateWithLessonUser(idUser, dateTarget)
        else if (this == DirectionDay.PREV)
            return lessonsController.getPrevDateWithLesson(idUser, dateTarget)
        return null
    }

    suspend fun respondLesson(idUser: Int, idLesson: Int): ModelAnswer<ModelLesson>{
        val lesson = lessonsController.getLesson(idLesson, idUser) ?: return "Занятие не найдено".asError(HttpStatusCode.NotFound)
        return lesson.toModelLesson().asAnswer()
    }

    suspend fun respondConfirmLesson(idLesson: Int, idUser: Int): ModelAnswer<Boolean> {
        return lessonsController.setConfirmLesson(idLesson, idUser).asAnswer()
    }

    suspend fun respondDelayLessons(idUser: Int): ModelAnswer<List<ModelLesson>>{
        val lessons = lessonsController.getDelayLessons(idUser)
        return lessons.map { it.toModelLesson() }.asAnswer()
    }
}