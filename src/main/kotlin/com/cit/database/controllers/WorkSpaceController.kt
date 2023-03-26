package com.cit.database.controllers

import com.cit.database.tables.Lesson
import com.cit.enums.DirectionDay
import com.cit.lessonsController
import com.cit.models.ModelAnswer
import io.ktor.http.*
import java.time.LocalDate
import java.time.LocalDateTime

class WorkSpaceController {

    suspend fun respondPlanDate(
        date: LocalDate?,
        idUser: Int,
        directionDay: DirectionDay?,
        idCourse: Int?
    ): ModelAnswer<List<Lesson>>{
        var targetDate = date ?: lessonsController.getNearDateWithLessonsUser(idUser, LocalDateTime.now())
        targetDate = (if (directionDay == null) targetDate else directionDay.getDateDirectionByNow(idUser))
            ?: return ModelAnswer(HttpStatusCode.NotFound, isError = true, messageError = "Для указаной даты/направления даты занятий нет")
        val lessons = lessonsController.getLessonsUserByDate(idUser, targetDate).filter { it.idCourse == idCourse }
        return ModelAnswer(answer = lessons)
    }

    private suspend fun DirectionDay.getDateDirectionByNow(idUser: Int): LocalDate?{
        if (this == DirectionDay.NEXT)
            return lessonsController.getNextDateWithLessonUser(idUser, LocalDate.now())
        else if (this == DirectionDay.PREV)
            return lessonsController.getPrevDateWithLesson(idUser, LocalDate.now())
        return null
    }
}