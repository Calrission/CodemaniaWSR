package com.cit.plugins

import com.cit.identityController
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.utils.receiveQueryParameter
import com.cit.utils.receiveUserByHeaderTokenOrIdUser
import com.cit.utils.receiveValidation
import com.cit.utils.respondAnswer
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureIdentityRouting(){

    routing {
        post("signIn") {
            val body = call.receiveValidation<SignInBody>() ?: return@post
            call.respondAnswer(identityController.signIn(body))
        }

        post("signUp"){
            val body = call.receiveValidation<SignUpBody>() ?: return@post
            call.respondAnswer(identityController.signUp(body))
        }

        post("signOut"){
            val user = call.receiveUserByHeaderTokenOrIdUser() ?: return@post
            call.respondAnswer(identityController.signOut(user.id))
        }

        get("checkToken"){
            val user = call.receiveUserByHeaderTokenOrIdUser()
            call.respondAnswer((user != null).asAnswer())
        }

        post("changePassword"){
            val user = call.receiveUserByHeaderTokenOrIdUser() ?: return@post
            val newPassword = call.receiveQueryParameter("newPassword") ?: return@post
            call.respondAnswer(identityController.respondChangePassword(user.id, newPassword))
        }
    }
}

