package com.cit.database

import com.cit.database.tables.*
import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory{
    fun initDataBase(){
        val driverClassName = "org.postgresql.Driver"
        val hostDB = getLocalProperty("host_db")
        val portDB = getLocalProperty("port_db")
        val dbName = getLocalProperty("name_db")
        val url = "jdbc:postgresql://${hostDB}:${portDB}/${dbName}"
        val user = getLocalProperty("user_db")
        val password = getLocalProperty("password_db")
        val database = Database.connect(url, driverClassName, user, password)

        createTablesIfNotExist(database)
    }

    private fun createTablesIfNotExist(database: Database){
        transaction(database){
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
            SchemaUtils.create(Tags)
            SchemaUtils.create(TagTypes)
            SchemaUtils.create(Roles)
            SchemaUtils.create(TagsCourses)
            SchemaUtils.create(Courses)
            SchemaUtils.create(CourseMentors)
            SchemaUtils.create(SoldCourses)
            SchemaUtils.create(FormatsLesson)
            SchemaUtils.create(TypesLesson)
            SchemaUtils.create(TypesChat)
            SchemaUtils.create(Materials)
            SchemaUtils.create(RolesUsers)
            SchemaUtils.create(TemplatesPlans)
            SchemaUtils.create(Lessons)
        }
    }

    suspend fun <T> pushQuery(block: () -> T): T{
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }
}
