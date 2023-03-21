package com.cit.database.tables

import org.jetbrains.exposed.sql.Table

data class RoleUser(
    val id: Int,
    val idUser: Int,
    val idRole: Int
)

data class RoleUserBody(
    val idUser: Int,
    val idRole: Int
)

object RolesUsers: Table() {
    val id = integer("id").autoIncrement()
    val idUser = integer("idUser")
    val idRole = integer("idRole")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}