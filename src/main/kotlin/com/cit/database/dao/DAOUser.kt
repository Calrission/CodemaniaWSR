package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.User
import com.cit.database.tables.UserBody
import com.cit.database.tables.Users
import com.cit.enums.Sex.Companion.isSex
import com.cit.models.bodies.SignUpBody
import com.cit.utils.DateTimeUtils.Companion.parseDate
import org.jetbrains.exposed.sql.*

class DAOUser: DAOTable<User, Users, UserBody>() {
    override fun resultRowToModel(row: ResultRow): User {
        return User(
            id = row[Users.id],
            firstname = row[Users.firstname],
            lastname = row[Users.lastname],
            patronymic = row[Users.patronymic],
            password = row[Users.password],
            email = row[Users.email],
            sex = row[Users.sex],
            dateBirthDay = row[Users.dateBirthDay],
            avatar = row[Users.avatar]
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

    override suspend fun insert(model: UserBody): User? {
        return pushQuery {
            Users.insert {
                it[firstname] = model.firstname
                it[lastname] = model.lastname
                it[patronymic] = model.patronymic
                it[sex] = model.sex
                it[email] = model.email
                it[password] = model.password
                it[dateBirthDay] = model.dateBirthDay
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Users.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.deleteWhere(op = where) > 0
        }
    }

    override suspend fun edit(model: UserBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.update(where) {
                it[firstname] = model.firstname
                it[lastname] = model.lastname
                it[patronymic] = model.patronymic
                it[sex] = model.sex
                it[email] = model.email
                it[password] = model.password
                it[dateBirthDay] = model.dateBirthDay
            } > 0
        }
    }
}