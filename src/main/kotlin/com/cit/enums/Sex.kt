package com.cit.enums

enum class Sex(val valueStr: String, val valueInt: Int) {
    MALE("Мужчина",0), FEMALE("Женщина",1);

    companion object {
        fun getAllValueStr(): List<String> = Sex.values().map { it.valueStr }
        fun getAllValueInt(): List<Int> = Sex.values().map { it.valueInt }

        fun String.isSex(): Sex? = Sex.values().singleOrNull { it.valueStr == this }

        fun Int.isSex(): Sex? = Sex.values().singleOrNull { it.valueInt == this }
    }
}