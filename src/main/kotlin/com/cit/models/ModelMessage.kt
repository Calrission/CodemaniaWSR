package com.cit.models

import com.cit.database.tables.ModelHuman
import com.cit.utils.DateTimeUtils
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ModelMessage(
    val message: String,
    val user: ModelHuman,
    var isYou: Boolean = false,
    val datetime: String = LocalDateTime.now().format(DateTimeUtils.dateTimeFormatter)
)
