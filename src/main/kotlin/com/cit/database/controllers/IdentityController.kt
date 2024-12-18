package com.cit.database.controllers

import com.cit.database.dao.DAOToken
import com.cit.database.dao.DAOUser
import com.cit.database.tables.*
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import io.ktor.http.*
import org.jetbrains.exposed.sql.and
import java.util.UUID

class IdentityController {
    private val daoUser = DAOUser()
    private val daoToken = DAOToken()

    suspend fun signIn(signInBody: SignInBody): ModelAnswer<IdentityResponse>{
        val user = daoUser.getUser(signInBody) ?:
            return "Пользователь не найден".asError(HttpStatusCode.NotFound)

        return respondIdentity(user)
    }

    suspend fun signUp(signUpBody: SignUpBody): ModelAnswer<IdentityResponse>{
        if (daoUser.checkExistEmail(signUpBody.email)){
            return ModelAnswer.instanceError(HttpStatusCode.NotFound, "Такой пользователь уже существует")
        }

        val newUser = daoUser.insert(signUpBody.toUserBody())
            ?: return ModelAnswer.instanceError(message = "Пользователь не создан, попробуйте еще раз !")

        return respondIdentity(newUser)
    }

    private suspend fun respondIdentity(user: User): ModelAnswer<IdentityResponse>{
        var token = daoToken.selectSingle { Tokens.idUser eq user.id }?.token
        if (token == null){
            token = setNewTokenUser(user.id)
                ?: return "Токен не создан, попробуйте еще раз !".asError(HttpStatusCode.NotFound)
        }

        return IdentityResponse(user.toPersonData(), token).asAnswer()
    }

    suspend fun signOut(idUser: Int): ModelAnswer<Boolean>{
        return true.asAnswer()
    }

    suspend fun setNewTokenUser(userId: Int): String?{
        val newTokenBody = TokenBody(UUID.randomUUID().toString(), userId)
        return if (daoToken.selectSingle { Tokens.idUser eq userId } == null){
            daoToken.insert(newTokenBody)?.token
        }else{
            daoToken.editAndSelect(newTokenBody){
                Tokens.idUser eq userId
            }?.token
        }
    }

    suspend fun respondChangePassword(idUser: Int, newPassword: String): ModelAnswer<Boolean>{
        return daoUser.updatePassword(idUser, newPassword).asAnswer()
    }

    suspend fun respondCheckValidToken(token: String, idUser: Int): ModelAnswer<Boolean>{
        return (daoToken.selectSingle { (Tokens.idUser eq idUser).and(Tokens.token eq token) } != null).asAnswer()
    }
}