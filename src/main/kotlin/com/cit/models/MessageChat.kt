package com.cit.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageChat(
    val id: Int,
    val message: String,
    val idUser: Int,
    var isYou: Boolean = false,
    val datetime: String,
    val isAudio: Boolean
)
