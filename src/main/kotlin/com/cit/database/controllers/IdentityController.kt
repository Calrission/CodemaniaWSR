package com.cit.database.controllers

import com.cit.database.dao.DAOToken
import com.cit.database.dao.DAOUser
import com.cit.database.tables.*
import com.cit.models.bodies.IdentityBody
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.and
import java.util.UUID

class IdentityController {
    private val daoUser = DAOUser()
    private val daoToken = DAOToken()

    suspend fun signIn(call: ApplicationCall, signInBody: SignInBody){
        val user = getUser(signInBody)

        if (user == null){
            call.respondError(HttpStatusCode.NotFound, "Пользователь не найден")
            return
        }

        respondNewToken(call, user.id)
    }

    suspend fun signUp(call: ApplicationCall, signUpBody: SignUpBody){
        if (checkExistEmail(signUpBody.email)){
            call.respondError(HttpStatusCode.NotFound, "Такой пользователь уже существует")
            return
        }

        val newUser = daoUser.insert(signUpBody)
        if (newUser == null) {
            call.respondError(error = "Пользователь не создан, попробуйте еще раз !")
            return
        }

        respondNewToken(call, newUser.id)
    }

    suspend fun respondNewToken(applicationCall: ApplicationCall, userId: Int){
        val token = setNewTokenUser(userId)

        if (token == null){
            applicationCall.respondError(error = "Токен не создан, попробуйте еще раз !")
            return
        }

        applicationCall.respond(token as SafeToken)
    }

    suspend fun setNewTokenUser(userId: Int): Token?{
        val newTokenBody = TokenBody(UUID.randomUUID().toString(), userId)
        return if (daoToken.selectSingle { Tokens.idUser eq userId } == null){
            daoToken.insert(newTokenBody)
        }else{
            daoToken.editAndSelect(newTokenBody){
                Tokens.idUser eq userId
            }
        }
    }

    suspend fun getUser(identityBody: IdentityBody): User?{
        return daoUser.selectSingle { (Users.email eq identityBody.email).and(Users.password eq identityBody.password) }
    }

    suspend fun checkExistEmail(email: String): Boolean{
        return daoUser.selectSingle { Users.email eq email } != null
    }
}