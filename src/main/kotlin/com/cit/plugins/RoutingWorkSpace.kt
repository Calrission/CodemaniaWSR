package com.cit.plugins

import com.cit.enums.DirectionDay.Companion.asDirectionDay
import com.cit.utils.*
import com.cit.utils.DateTimeUtils.Companion.datePattern
import com.cit.utils.DateTimeUtils.Companion.parseDate
import com.cit.workSpaceController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureWorkSpace(){
    routing {
        get("workSpace/plan"){
            val date = call.receiveQueryValidateParameter("date", respondError = false) {
                ValidationUtils.localDateValidation(it, datePattern)
            }?.parseDate()
            val idCourse = call.receiveQueryParameter("courseId", respondError = false)?.toInt()
            val direction = call.receiveQueryValidateParameter("direction", respondError = false){
                ValidationUtils.directionDayValidation(it)
            }?.asDirectionDay()
            val user = call.receiveUserByHeaderToken() ?: return@get
            call.respondAnswer(workSpaceController.respondPlanDate(date, user.id, direction, idCourse))
        }

        get("workSpace/lesson"){
            val user = call.receiveUserByHeaderToken() ?: return@get
            val idLesson = call.receiveQueryParameter("idLesson")?.toInt() ?: return@get

            call.respondAnswer(workSpaceController.respondLesson(user.id, idLesson))
        }

        post("workSpace/confirmLesson"){
            val user = call.receiveUserByHeaderToken() ?: return@post
            val idLesson = call.receiveQueryParameter("idLesson")?.toInt() ?: return@post

            call.respondAnswer(workSpaceController.respondConfirmLesson(idLesson, user.id))
        }

        get("workSpace/delayLessons"){
            val user = call.receiveUserByHeaderToken() ?: return@get

            call.respondAnswer(workSpaceController.respondDelayLessons(user.id))
        }
    }
}