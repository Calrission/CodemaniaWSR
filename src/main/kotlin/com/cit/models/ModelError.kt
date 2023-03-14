package com.cit.models

import kotlinx.serialization.Serializable

@Serializable
data class ModelError(
    val error: String
)