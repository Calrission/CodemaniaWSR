package com.cit.plugins

import com.cit.identityController
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.utils.receiveValidation
import com.cit.utils.respondModelAnswer
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureIdentityRouting(){

    routing {
        post("signIn") {
            val body = call.receiveValidation<SignInBody>() ?: return@post
            call.respondModelAnswer(identityController.signIn(body))
        }

        post("signUp"){
            val body = call.receiveValidation<SignUpBody>() ?: return@post
            call.respondModelAnswer(identityController.signUp(body))
        }
    }
}

