package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.Course
import com.cit.database.tables.CourseBody
import com.cit.database.tables.Courses
import org.jetbrains.exposed.sql.*

class DAOCourses: DAOTable<Course, Courses, CourseBody, CourseBody>() {
    override fun resultRowToModel(row: ResultRow): Course {
        return Course(
            id = row[Courses.id],
            title = row[Courses.title],
            description = row[Courses.description],
            cover = row[Courses.cover],
            plan = row[Courses.plan],
            price = row[Courses.price]
        )
    }

    override suspend fun selectAll(): List<Course> {
        return DatabaseFactory.pushQuery {
            Courses.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Course? {
        return DatabaseFactory.pushQuery {
            Courses.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Course> {
        return DatabaseFactory.pushQuery {
            Courses.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Courses.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Courses.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: CourseBody): Course? {
        return DatabaseFactory.pushQuery {
            Courses.insert {
                it[title] = model.title
                it[description] = model.description
                it[cover] = model.cover
                it[plan] = model.plan
                it[price] = model.price
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: CourseBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Courses.update(where) {
                it[title] = model.title
                it[description] = model.description
                it[cover] = model.cover
                it[plan] = model.plan
                it[price] = model.price
            } > 0
        }
    }
}