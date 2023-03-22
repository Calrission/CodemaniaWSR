package com.cit.database.controllers

import com.cit.database.dao.DAOToken
import com.cit.database.dao.DAOUser
import com.cit.database.tables.*
import com.cit.models.ModelAnswer
import com.cit.models.bodies.IdentityBody
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.h2.engine.Mode
import org.jetbrains.exposed.sql.and
import java.util.UUID

class IdentityController {
    private val daoUser = DAOUser()
    private val daoToken = DAOToken()

    suspend fun signIn(signInBody: SignInBody): ModelAnswer<ModelToken>{
        val user = daoUser.getUser(signInBody) ?: return ModelAnswer.instanceError(HttpStatusCode.NotFound, "Пользователь не найден")

        return respondNewToken(user.id)
    }

    suspend fun signUp(signUpBody: SignUpBody): ModelAnswer<ModelToken>{
        if (daoUser.checkExistEmail(signUpBody.email)){
            return ModelAnswer.instanceError(HttpStatusCode.NotFound, "Такой пользователь уже существует")
        }

        val newUser = daoUser.insert(signUpBody.toUserBody())
            ?: return ModelAnswer.instanceError(message = "Пользователь не создан, попробуйте еще раз !")

        return respondNewToken(newUser.id)
    }

    suspend fun respondNewToken(userId: Int): ModelAnswer<ModelToken>{
        val token = setNewTokenUser(userId)
            ?: return ModelAnswer.instanceError(HttpStatusCode.BadRequest, "Токен не создан, попробуйте еще раз !")

        return ModelAnswer(answer = token as ModelToken)
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
}