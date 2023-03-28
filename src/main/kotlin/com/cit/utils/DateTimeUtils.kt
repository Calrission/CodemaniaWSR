package com.cit.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.Temporal

class DateTimeUtils {
    companion object {
        const val datePattern = "dd.MM.yyyy"
        const val timePattern = "HH:mm"
        const val datetimePattern = "$datePattern $timePattern"
        const val filenamePattern = "ddMMyyyyHHmm"

        val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
        val timeFormatter = DateTimeFormatter.ofPattern(timePattern)
        val dateTimeFormatter = DateTimeFormatter.ofPattern(datetimePattern)
        val filenamePatternFormatter = DateTimeFormatter.ofPattern(filenamePattern)

        fun String.parseDateFormatter(formatter: DateTimeFormatter): LocalDate? {
            return try {
                LocalDate.parse(this, formatter)
            }catch (e: DateTimeParseException){
                null
            }
        }

        fun String.parseDateTimeFormatter(formatter: DateTimeFormatter): LocalDateTime? {
            return try {
                LocalDateTime.parse(this, formatter)
            }catch (e: DateTimeParseException){
                null
            }
        }
        fun String.parseDate(): LocalDate? = parseDateFormatter(dateFormatter)
        fun String.parseTime(): LocalDate? = parseDateFormatter(timeFormatter)
        fun String.parseDateTime(): LocalDateTime? = parseDateTimeFormatter(dateTimeFormatter)
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