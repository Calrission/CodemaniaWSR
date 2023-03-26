package com.cit.enums

enum class DirectionDay(val message: String){
    NEXT("next"), PREV("prev");
    companion object{
        fun String.asDirectionDay(): DirectionDay = values().single { it.message == this }
        fun getAllMessages(): List<String> = DirectionDay.values().map { it.message }
    }
}