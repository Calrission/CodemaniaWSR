package com.cit.models.bodies

import kotlinx.serialization.Serializable

@Serializable
data class SignInBody(
    override val email: String,
    override val password: String
): IdentityBase()



