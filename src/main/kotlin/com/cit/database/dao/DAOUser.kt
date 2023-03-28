package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.User
import com.cit.database.tables.InsertUserBody
import com.cit.database.tables.PatchUserBody
import com.cit.database.tables.Users
import com.cit.enums.Sex.Companion.isSex
import com.cit.models.bodies.IdentityBody
import org.jetbrains.exposed.sql.*

class DAOUser: DAOTable<User, Users, InsertUserBody, PatchUserBody>() {
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

    override suspend fun insert(model: InsertUserBody): User? {
        return pushQuery {
            Users.insert {
                it[firstname] = model.firstname
                it[lastname] = model.lastname
                it[patronymic] = model.patronymic
                it[sex] = model.sex
                it[avatar] = model.avatar
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

    override suspend fun edit(model: PatchUserBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.update(where) {
                if (model.firstname != null)
                    it[firstname] = model.firstname
                if (model.lastname != null)
                    it[lastname] = model.lastname
                if (model.patronymic != null)
                    it[patronymic] = model.patronymic
                if (model.sex != null)
                    it[sex] = model.sex.valueInt
                if (model.email != null)
                    it[email] = model.email
                if (model.dateBirthDay != null)
                    it[dateBirthDay] = model.dateBirthDay
            } > 0
        }
    }

    suspend fun getUser(identityBody: IdentityBody): User?{
        return selectSingle { (Users.email eq identityBody.email).and(Users.password eq identityBody.password) }
    }

    suspend fun checkExistEmail(email: String): Boolean{
        return selectSingle { Users.email eq email } != null
    }

    suspend fun updateAvatar(avatar: String, idUser: Int): Boolean{
        return pushQuery {
            Users.update({
                Users.id eq idUser
            }){
                it[Users.avatar] = avatar
            }
        } > 0
    }
}