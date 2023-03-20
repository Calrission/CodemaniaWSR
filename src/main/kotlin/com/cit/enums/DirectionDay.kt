package com.cit.enums

enum class DirectionDay(val message: String){
    NEXT("next"), PREV("prev");

    fun getAllMessages(): List<String> = DirectionDay.values().map { it.message }
}