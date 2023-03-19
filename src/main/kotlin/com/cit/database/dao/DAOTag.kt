package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.*
import org.jetbrains.exposed.sql.*

class DAOTag: DAOTable<Tag, Tags, Tag>() {
    override fun resultRowToModel(row: ResultRow): Tag {
        return Tag(
            id = row[Tags.id],
            name = row[Tags.name],
            typeId = row[Tags.typeId]
        )
    }

    override suspend fun selectAll(): List<Tag> {
        return pushQuery {
            Tags.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Tag? {
        return pushQuery {
            Tags.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Tag> {
        return pushQuery {
            Tags.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Tags.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            Tags.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: Tag): Tag? {
        return pushQuery {
            Tags.insert {
                it[name] = model.name
                it[typeId] = typeId
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: Tag, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Tags.update(where) {
                it[name] = model.name
                it[typeId] = model.typeId
            } > 0
        }
    }

}