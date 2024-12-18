package com.cit.database.controllers

import com.cit.database.dao.DAOLessons
import com.cit.database.tables.Lesson
import com.cit.database.tables.LessonBody
import com.cit.database.tables.Lessons
import org.jetbrains.exposed.sql.and
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class LessonsController {

    private val daoLesson = DAOLessons()

    suspend fun getLesson(idLesson: Int, idUser: Int): Lesson?{
        return daoLesson.selectSingle {
            (Lessons.idUser eq idUser).and(Lessons.id eq idLesson)
        }
    }

    suspend fun setConfirmLesson(idLesson: Int, idUser: Int): Boolean{
        return daoLesson.edit(true){
            (Lessons.idUser eq idUser).and(Lessons.id eq idLesson)
        }
    }

    suspend fun getAllLessonsUser(userId: Int): List<Lesson>{
        return daoLesson.selectMany {
            Lessons.idUser eq userId
        }
    }

    suspend fun getLessonsUserByDate(idUser: Int, date: LocalDate): List<Lesson>{
        return daoLesson.selectMany {
            (Lessons.datetime.between(date.atTime(0, 0, 0), date.atTime(23, 59, 59)))
                .and(Lessons.idUser.eq(idUser))
        }
    }

    suspend fun getDelayLessons(idUser: Int): List<Lesson>{
        return daoLesson.selectMany {
            Lessons.datetime
                .between(LocalDateTime.of(0, 1, 1, 0, 0, 0), LocalDateTime.now())
                .and(Lessons.idUser eq idUser)
                .and(Lessons.isComplete eq false)
        }
    }

    suspend fun getNearDateWithLessonsUser(idUser: Int, targetDateTime: LocalDateTime): LocalDate? {
        val targetDate = targetDateTime.toLocalDate()
        val targetTime = targetDateTime.toLocalTime()
        val targetUnix = targetDate.atTime(targetTime).toEpochSecond(ZoneOffset.UTC)

        val nextDate = getNextInclusiveDateWithLessonUser(idUser, targetDate)
        val prevDate = getPrevInclusiveDateWithLesson(idUser, targetDate)

        val nextDateUnix = nextDate?.atTime(targetTime)?.toEpochSecond(ZoneOffset.UTC)
        val prevDateUnix = prevDate?.atTime(targetTime)?.toEpochSecond(ZoneOffset.UTC)

        return if (nextDateUnix != null && prevDateUnix != null) {
            val diffNextDate = nextDateUnix - targetUnix
            val diffPrevDate = targetUnix - prevDateUnix
            if (diffNextDate <= diffPrevDate) nextDate else prevDate
        }else if (nextDateUnix != null){
            nextDate
        }else if (prevDateUnix != null){
            prevDate
        }else{
            null
        }
    }

    suspend fun getNextDateWithLessonUser(idUser: Int, startDate: LocalDate): LocalDate?{
        val lessons = getAllLessonsUser(idUser)
        val nextDate = lessons
            .filter { it.datetime.toLocalDate().isAfter(startDate) }
            .minOfOrNull { it.datetime.toLocalDate() }
        return nextDate
    }

    suspend fun getPrevDateWithLesson(idUser: Int, endDate: LocalDate): LocalDate?{
        val lessons = getAllLessonsUser(idUser)
        val prevDate = lessons
            .filter { it.datetime.toLocalDate().isBefore(endDate) }
            .maxOfOrNull { it.datetime.toLocalDate() }
        return prevDate
    }

    suspend fun getNextInclusiveDateWithLessonUser(idUser: Int, startDate: LocalDate): LocalDate?{
        val lessons = getAllLessonsUser(idUser)
        val nextDate = lessons
            .filter { !it.datetime.toLocalDate().isBefore(startDate) }
            .minOfOrNull { it.datetime.toLocalDate() }
        return nextDate
    }

    suspend fun getPrevInclusiveDateWithLesson(idUser: Int, endDate: LocalDate): LocalDate?{
        val lessons = getAllLessonsUser(idUser)
        val prevDate = lessons
            .filter { !it.datetime.toLocalDate().isAfter(endDate) }
            .maxOfOrNull { it.datetime.toLocalDate() }
        return prevDate
    }

    suspend fun getLessonsCourseUser(idCourse: Int, idUser: Int): List<Lesson>{
        return daoLesson.selectMany {
            (Lessons.idUser eq idUser).and(Lessons.idCourse eq idCourse)
        }
    }

    suspend fun insertNewLessons(bodies: List<LessonBody>): List<Boolean> {
        return bodies.map { daoLesson.insert(it) != null }
    }
}