package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.ModelRole
import com.cit.database.tables.RoleBody
import com.cit.database.tables.Roles
import org.jetbrains.exposed.sql.*

class DAORoles: DAOTable<ModelRole, Roles, RoleBody>() {
    override fun resultRowToModel(row: ResultRow): ModelRole {
        return ModelRole(
            id = row[Roles.id],
            name = row[Roles.name]
        )
    }

    override suspend fun selectAll(): List<ModelRole> {
        return DatabaseFactory.pushQuery {
            Roles.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): ModelRole? {
        return DatabaseFactory.pushQuery {
            Roles.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<ModelRole> {
        return DatabaseFactory.pushQuery {
            Roles.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Roles.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Roles.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: RoleBody): ModelRole? {
        return DatabaseFactory.pushQuery {
            Roles.insert {
                it[name] = model.name
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: RoleBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Roles.update(where) {
                it[name] = model.name
            } > 0
        }
    }
}