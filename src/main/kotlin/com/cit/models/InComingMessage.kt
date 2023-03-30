package com.cit.models

data class InComingMessage (
    val text: String?,
    val idChat: Int?,
    val isAudio: Boolean = false
)