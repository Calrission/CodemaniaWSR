package com.cit.database.controllers

import com.cit.database.dao.DAOCourseMentors
import com.cit.database.dao.DAOToken
import com.cit.database.dao.DAOUser
import com.cit.database.tables.*
import com.cit.utils.checkExistImage
import com.cit.utils.removeImage

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
        val oldAvatar = (daoUser.selectSingle { Users.id eq idUser } ?: return false).avatar
        val resultUpdate = daoUser.updateAvatar(avatarName, idUser)
        return if (resultUpdate) {
            oldAvatar != null && (!checkExistImage(oldAvatar) || removeImage(oldAvatar))
        }else{
            false
        }
    }

    suspend fun checkExistEmail(email: String): Boolean = daoUser.checkExistEmail(email)

    private suspend fun getMentorsIdsCourse(idCourse: Int): List<Int> = daoCourseMentor.selectMany {
        CourseMentors.idCourse eq idCourse
    }.map { it.idUser }

    suspend fun patchUser(idUser: Int, userBody: PatchUserBody): User?{
        return daoUser.editAndSelect(userBody){
            Users.id eq idUser
        }
    }
}