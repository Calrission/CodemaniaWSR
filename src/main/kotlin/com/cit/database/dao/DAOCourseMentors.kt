package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.CourseMentor
import com.cit.database.tables.CourseMentors
import org.jetbrains.exposed.sql.*

class DAOCourseMentors: DAOTable<CourseMentor, CourseMentors, CourseMentor>() {
    override fun resultRowToModel(row: ResultRow): CourseMentor {
        return CourseMentor(
            id = row[CourseMentors.id],
            idCourse = row[CourseMentors.idCourse],
            idMentor = row[CourseMentors.idMentor]
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

    override suspend fun insert(model: CourseMentor): CourseMentor? {
        return DatabaseFactory.pushQuery {
            CourseMentors.insert {
                it[idCourse] = model.idCourse
                it[idMentor] = model.idMentor
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: CourseMentor, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            CourseMentors.update(where) {
                it[idCourse] = model.idCourse
                it[idMentor] = model.idMentor
            } > 0
        }
    }
    
}