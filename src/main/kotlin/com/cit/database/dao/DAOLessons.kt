package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.*
import org.jetbrains.exposed.sql.*

class DAOLessons: DAOTable<Lesson, Lessons, LessonBody, LessonBody>() {
    override fun resultRowToModel(row: ResultRow): Lesson {
        return Lesson(
            id = row[Lessons.id],
            idCourse = row[Lessons.idCourse],
            idUser = row[Lessons.idUser],
            title = row[Lessons.title],
            description = row[Lessons.description],
            datetime = row[Lessons.datetime],
            duration = row[Lessons.duration],
            isComplete = row[Lessons.isComplete]
        )
    }

    override suspend fun selectAll(): List<Lesson> {
        return pushQuery {
            Lessons.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Lesson? {
        return pushQuery {
            Lessons.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Lesson> {
        return pushQuery {
            Lessons.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Lessons.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            Lessons.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: LessonBody): Lesson? {
        return pushQuery {
            Lessons.insert {
                it[title] = model.title
                it[description] = model.description
                it[idCourse] = model.idCourse
                it[datetime] = model.datetime
                it[duration] = model.duration
                it[isComplete] = model.isComplete
                it[idUser] = model.idUser
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: LessonBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Lessons.update(where) {
                it[title] = model.title
                it[description] = model.description
                it[idCourse] = model.idCourse
                it[datetime] = model.datetime
                it[duration] = model.duration
                it[isComplete] = model.isComplete
                it[idUser] = model.idUser
            } > 0
        }
    }

    suspend fun edit(confirmLesson: Boolean, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Lessons.update(where) {
                it[isComplete] = confirmLesson
            } > 0
        }
    }
}