package com.cit.plugins

import com.cit.identityController
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.utils.receiveUserByQueryToken
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
            val user = call.receiveUserByQueryToken() ?: return@post
            call.respondAnswer(identityController.signOut(user.id))
        }
    }
}

