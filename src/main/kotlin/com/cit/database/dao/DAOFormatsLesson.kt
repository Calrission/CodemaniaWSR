package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.FormatLesson
import com.cit.database.tables.FormatsLesson
import org.jetbrains.exposed.sql.*

class DAOFormatsLesson: DAOTable<FormatLesson, FormatsLesson, FormatLesson>() {
    override fun resultRowToModel(row: ResultRow): FormatLesson {
        return FormatLesson(
            id = row[FormatsLesson.id],
            name = row[FormatsLesson.name]
        )
    }

    override suspend fun selectAll(): List<FormatLesson> {
        return DatabaseFactory.pushQuery {
            FormatsLesson.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): FormatLesson? {
        return DatabaseFactory.pushQuery {
            FormatsLesson.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<FormatLesson> {
        return DatabaseFactory.pushQuery {
            FormatsLesson.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: FormatsLesson.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            FormatsLesson.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: FormatLesson): FormatLesson? {
        return DatabaseFactory.pushQuery {
            FormatsLesson.insert {
                it[name] = model.name
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: FormatLesson, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            FormatsLesson.update(where) {
                it[name] = model.name
            } > 0
        }
    }
}