package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.User
import com.cit.database.tables.Users
import com.cit.models.bodies.SignUpBody
import org.jetbrains.exposed.sql.*

class DAOUser: DAOTable<User, Users, SignUpBody>() {
    override fun resultRowToModel(row: ResultRow): User {
        return User(
            id = row[Users.id],
            login = row[Users.login],
            password = row[Users.password],
            email = row[Users.email]
        )
    }

    override suspend fun selectAll(): List<User> {
        return pushQuery {
            Users.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): User? {
       return pushQuery {
           Users.select(where).map { resultRowToModel(it) }.singleOrNull()
       }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<User> {
        return pushQuery {
            Users.select(where)
                .map(::resultRowToModel)
        }

    }

    override suspend fun insert(model: SignUpBody): User? {
        return pushQuery {
            Users.insert {
                it[login] = model.login
                it[email] = model.email
                it[password] = model.password
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Users.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.deleteWhere(op = where) > 0
        }
    }

    override suspend fun edit(model: SignUpBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.update(where) {
                it[login] = model.login
                it[email] = model.email
                it[password] = model.password
            } > 0
        }
    }
}