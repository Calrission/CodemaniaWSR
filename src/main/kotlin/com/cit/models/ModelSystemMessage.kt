package com.cit.models

import kotlinx.serialization.Serializable

@Serializable
data class ModelSystemMessage(
    val message: String,
    val isError: Boolean = false
){
    companion object {
        fun String.isModelSystemMessage(): ModelSystemMessage = ModelSystemMessage(this)
        fun String.isModelErrorSystemMessage(): ModelSystemMessage = ModelSystemMessage(this, true)
    }
}
