package com.cit.database.controllers

import com.cit.database.tables.Lesson
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
    ): ModelAnswer<List<Lesson>>{
        var targetDate = date ?: lessonsController.getNearDateWithLessonsUser(idUser, LocalDateTime.now())
        targetDate = (if (directionDay == null) targetDate else directionDay.getDateDirectionByNow(idUser))
            ?: return "Для указаной даты/направления даты занятий нет".asError(HttpStatusCode.NotFound)
        val lessons = lessonsController.getLessonsUserByDate(idUser, targetDate).filter { it.idCourse == idCourse }
        return lessons.asAnswer()
    }

    private suspend fun DirectionDay.getDateDirectionByNow(idUser: Int): LocalDate?{
        if (this == DirectionDay.NEXT)
            return lessonsController.getNextDateWithLessonUser(idUser, LocalDate.now())
        else if (this == DirectionDay.PREV)
            return lessonsController.getPrevDateWithLesson(idUser, LocalDate.now())
        return null
    }

    suspend fun respondLesson(idUser: Int, idLesson: Int): ModelAnswer<ModelLesson>{
        val lesson = lessonsController.getLesson(idLesson, idUser) ?: return ModelAnswer(HttpStatusCode.NotFound, messageError = "Занятие не найдено")
        return lesson.toModelLesson().asAnswer()
    }

    suspend fun respondConfirmLesson(idLesson: Int, idUser: Int): ModelAnswer<Boolean> {
        return lessonsController.setConfirmLesson(idLesson, idUser).asAnswer()
    }

    suspend fun respondDelayLessons(idUser: Int): ModelAnswer<List<ModelLesson>>{
        return lessonsController.getDelayLessons(idUser).map() { it.toModelLesson() }.asAnswer()
    }
}