package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.*
import org.jetbrains.exposed.sql.*

class DAOTagType: DAOTable<ModelTagType, TagTypes, ModelTagType>() {
    override fun resultRowToModel(row: ResultRow): ModelTagType {
        return ModelTagType(
            id = row[TagTypes.id],
            name = row[TagTypes.name]
        )
    }

    override suspend fun selectAll(): List<ModelTagType> {
        return pushQuery {
            TagTypes.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): ModelTagType? {
        return pushQuery {
            TagTypes.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<ModelTagType> {
        return pushQuery {
            TagTypes.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: TagTypes.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            TagTypes.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: ModelTagType): ModelTagType? {
        return pushQuery {
            TagTypes.insert {
                it[name] = model.name
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: ModelTagType, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            TagTypes.update(where) {
                it[name] = model.name
            } > 0
        }
    }

}