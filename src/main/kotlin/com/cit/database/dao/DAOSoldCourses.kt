package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.SoldCourse
import com.cit.database.tables.SoldCourseBody
import com.cit.database.tables.SoldCourses
import org.jetbrains.exposed.sql.*

class DAOSoldCourses: DAOTable<SoldCourse, SoldCourses, SoldCourseBody>() {
    override fun resultRowToModel(row: ResultRow): SoldCourse {
        return SoldCourse(
            id = row[SoldCourses.id],
            idUser = row[SoldCourses.idUser],
            idCourse = row[SoldCourses.idCourse],
            startEducation = row[SoldCourses.startEducation]
        )
    }

    override suspend fun selectAll(): List<SoldCourse> {
        return DatabaseFactory.pushQuery {
            SoldCourses.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): SoldCourse? {
        return DatabaseFactory.pushQuery {
            SoldCourses.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<SoldCourse> {
        return DatabaseFactory.pushQuery {
            SoldCourses.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: SoldCourses.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            SoldCourses.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: SoldCourseBody): SoldCourse? {
        return DatabaseFactory.pushQuery {
            SoldCourses.insert {
                it[idCourse] = model.idCourse
                it[idUser] = model.idUser
                it[startEducation] = model.startEducation
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: SoldCourseBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            SoldCourses.update(where) {
                it[idCourse] = model.idCourse
                it[idUser] = model.idUser
                it[startEducation] = model.startEducation
            } > 0
        }
    }
}