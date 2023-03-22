package com.cit.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateTimeUtils {
    companion object {
        const val datePattern = "dd.MM.yyyy"
        const val timePattern = "HH:mm"
        const val filenamePattern = "ddMMyyyyHHmm"

        val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
        val timeFormatter = DateTimeFormatter.ofPattern(timePattern)
        val filenamePatternFormatter = DateTimeFormatter.ofPattern(filenamePattern)

        fun String.parseFormatter(formatter: DateTimeFormatter): LocalDate? {
            return try {
                LocalDate.parse(this, formatter)
            }catch (e: DateTimeParseException){
                null
            }
        }
        fun String.parseDate(): LocalDate? = parseFormatter(dateFormatter)
        fun String.parseTime(): LocalDate? = parseFormatter(timeFormatter)
        fun nowDate(): String{
            return LocalDate.now().format(dateFormatter)
        }
        fun getDateTimeFilename(): String{
            return LocalDateTime.now().format(filenamePatternFormatter)
        }
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