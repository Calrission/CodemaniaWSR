package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.*
import org.jetbrains.exposed.sql.*

class DAORolesUsers: DAOTable<RoleUser, RolesUsers, RoleUserBody, RoleUserBody>() {
    override fun resultRowToModel(row: ResultRow): RoleUser {
        return RoleUser(
            id = row[RolesUsers.id],
            idUser = row[RolesUsers.idUser],
            idRole = row[RolesUsers.idRole]
        )
    }

    override suspend fun selectAll(): List<RoleUser> {
        return pushQuery {
            RolesUsers.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): RoleUser? {
        return pushQuery {
            RolesUsers.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<RoleUser> {
        return pushQuery {
            RolesUsers.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: RolesUsers.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            RolesUsers.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: RoleUserBody): RoleUser? {
        return pushQuery {
            RolesUsers.insert {
                it[idRole] = model.idRole
                it[idUser] = model.idUser
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: RoleUserBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            RolesUsers.update(where) {
                it[idRole] = model.idRole
                it[idUser] = model.idUser
            } > 0
        }
    }

}