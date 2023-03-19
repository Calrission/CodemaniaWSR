package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.TypeLesson
import com.cit.database.tables.TypesLesson
import org.jetbrains.exposed.sql.*

class DAOTypesLesson: DAOTable<TypeLesson, TypesLesson, TypeLesson>() {
    override fun resultRowToModel(row: ResultRow): TypeLesson {
        return TypeLesson(
            id = row[TypesLesson.id],
            name = row[TypesLesson.name]
        )
    }

    override suspend fun selectAll(): List<TypeLesson> {
        return DatabaseFactory.pushQuery {
            TypesLesson.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): TypeLesson? {
        return DatabaseFactory.pushQuery {
            TypesLesson.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<TypeLesson> {
        return DatabaseFactory.pushQuery {
            TypesLesson.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: TypesLesson.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TypesLesson.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: TypeLesson): TypeLesson? {
        return DatabaseFactory.pushQuery {
            TypesLesson.insert {
                it[name] = model.name
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: TypeLesson, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TypesLesson.update(where) {
                it[name] = model.name
            } > 0
        }
    }
}