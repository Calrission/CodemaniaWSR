package com.cit.utils

import com.cit.utils.DateTimeUtils.Companion.isValidDate
import com.cit.utils.DateTimeUtils.Companion.parseFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateTimeUtils {
    companion object {
        const val datePattern = "dd.MM.yyyy"
        const val timePattern = "HH:mm"

        val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
        val timeFormatter = DateTimeFormatter.ofPattern(timePattern)

        fun String.parseFormatter(formatter: DateTimeFormatter): LocalDate? {
            return try {
                LocalDate.parse(this, formatter)
            }catch (e: DateTimeParseException){
                null
            }
        }
        fun String.parseDate(): LocalDate? = parseFormatter(dateFormatter)
        fun String.parseTime(): LocalDate? = parseFormatter(timeFormatter)


        fun String.isValidFormatter(formatter: DateTimeFormatter): Boolean = try {
            LocalDate.parse(this, formatter)
            true
        }catch (e: DateTimeParseException){
            false
        }
        fun String.isValidDate(): Boolean = isValidFormatter(dateFormatter)
        fun String.isValidTime(): Boolean = isValidFormatter(timeFormatter)
    }
}