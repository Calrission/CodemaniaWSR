package com.cit.plugins

import com.cit.identityController
import com.cit.models.bodies.SignInBody
import com.cit.models.bodies.SignUpBody
import com.cit.usersController
import com.cit.utils.receiveUserByQueryToken
import com.cit.utils.receiveValidation
import com.cit.utils.respond
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureIdentityRouting(){

    routing {
        post("signIn") {
            val body = call.receiveValidation<SignInBody>() ?: return@post
            call.respond(identityController.signIn(body))
        }

        post("signUp"){
            val body = call.receiveValidation<SignUpBody>() ?: return@post
            call.respond(identityController.signUp(body))
        }

        post("signOut"){
            val user = call.receiveUserByQueryToken() ?: return@post
            call.respond(identityController.signOut(user.id))
        }
    }
}

