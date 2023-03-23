package com.cit.models

import com.cit.database.tables.ModelHuman
import kotlinx.serialization.Serializable

@Serializable
data class ModelMessage(
    val message: String,
    val user: ModelHuman
)
