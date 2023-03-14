package com.cit.plugins

import com.cit.database.controllers.IdentityController
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.utils.receiveValidation
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureIdentityRouting(){

    val identityController = IdentityController()

    routing {
        post("signIn") {
            val body = call.receiveValidation<SignInBody>() ?: return@post
            identityController.signIn(call, body)
        }

        post("signUp"){
            val body = call.receiveValidation<SignUpBody>() ?: return@post
            identityController.signUp(call, body)
        }
    }
}

