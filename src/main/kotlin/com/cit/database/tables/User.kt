package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

class User(
    val id: Int,
    val login: String,
    val email: String,
    val password: String
)

object Users: Table(){
    val id = integer("id").autoIncrement()
    val login = varchar("login", 100)
    val email = varchar("email", 100)
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(id)
}