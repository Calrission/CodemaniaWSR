package com.cit.database.controllers

import com.cit.database.dao.DAOCourseMentors
import com.cit.database.dao.DAOToken
import com.cit.database.dao.DAOUser
import com.cit.database.tables.*

class UsersController {

    private val daoCourseMentor = DAOCourseMentors()
    private val daoUser = DAOUser()
    private val daoToken = DAOToken()

    suspend fun getMentorsCourse(idCourse: Int): List<ModelHuman>{
        return getMentorsIdsCourse(idCourse).mapNotNull { getUser(it)?.toModelHuman() }
    }

    suspend fun getUserByToken(token: String): User? =
        daoToken.selectSingle { Tokens.token eq token }?.idUser?.let {getUser(it) }

    suspend fun getUser(idUser: Int): User? = daoUser.selectSingle { Users.id eq idUser }

    suspend fun checkValidToken(token: String): Boolean{
        return daoToken.selectSingle { Tokens.token eq token } != null
    }

    suspend fun updateAvatar(avatarName: String, idUser: Int): Boolean{
        return daoUser.updateAvatar(avatarName, idUser)
    }

    private suspend fun getMentorsIdsCourse(idCourse: Int): List<Int> = daoCourseMentor.selectMany {
        CourseMentors.idCourse eq idCourse
    }.map { it.idUser }

    suspend fun patchUser(idUser: Int, userBody: PatchUserBody): User?{
        return daoUser.editAndSelect(userBody){
            Users.id eq idUser
        }
    }
}