package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.TagCourse
import com.cit.database.tables.TagCourseBody
import com.cit.database.tables.TagsCourses
import org.jetbrains.exposed.sql.*

class DAOTagsCourse: DAOTable<TagCourse, TagsCourses, TagCourseBody, TagCourseBody>() {
    override fun resultRowToModel(row: ResultRow): TagCourse {
        return TagCourse(
            id = row[TagsCourses.id],
            idCourse = row[TagsCourses.idCourse],
            idTag = row[TagsCourses.idTag]
        )
    }

    override suspend fun selectAll(): List<TagCourse> {
        return DatabaseFactory.pushQuery {
            TagsCourses.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): TagCourse? {
        return DatabaseFactory.pushQuery {
            TagsCourses.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<TagCourse> {
        return DatabaseFactory.pushQuery {
            TagsCourses.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: TagsCourses.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TagsCourses.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: TagCourseBody): TagCourse? {
        return DatabaseFactory.pushQuery {
            TagsCourses.insert {
                it[idCourse] = model.idCourse
                it[idTag] = model.idTag
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: TagCourseBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TagsCourses.update(where) {
                it[idCourse] = model.idCourse
                it[idTag] = model.idTag
            } > 0
        }
    }
}