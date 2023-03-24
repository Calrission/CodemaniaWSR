package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.CourseMentor
import com.cit.database.tables.CourseMentorBody
import com.cit.database.tables.CourseMentors
import org.jetbrains.exposed.sql.*

class DAOCourseMentors: DAOTable<CourseMentor, CourseMentors, CourseMentorBody>() {
    override fun resultRowToModel(row: ResultRow): CourseMentor {
        return CourseMentor(
            id = row[CourseMentors.id],
            idCourse = row[CourseMentors.idCourse],
            idUser = row[CourseMentors.idUser]
        )
}

    override suspend fun selectAll(): List<CourseMentor> {
        return DatabaseFactory.pushQuery {
            CourseMentors.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): CourseMentor? {
        return DatabaseFactory.pushQuery {
            CourseMentors.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<CourseMentor> {
        return DatabaseFactory.pushQuery {
            CourseMentors.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: CourseMentors.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            CourseMentors.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: CourseMentorBody): CourseMentor? {
        return DatabaseFactory.pushQuery {
            CourseMentors.insert {
                it[idCourse] = model.idCourse
                it[idUser] = model.idUser
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: CourseMentorBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            CourseMentors.update(where) {
                it[idCourse] = model.idCourse
                it[idUser] = model.idUser
            } > 0
        }
    }
    
}