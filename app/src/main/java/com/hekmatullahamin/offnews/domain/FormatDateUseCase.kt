package com.hekmatullahamin.offnews.domain

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

//TODO: use format also
//class FormatDateUseCase @Inject constructor() {
//    operator fun invoke(
//        offsetDateTime: OffsetDateTime,
//        format: String = "yyyy-MM-dd - HH:mm:ss"
//    ): String {
//        return try {
//            val formatter = DateTimeFormatter.ofPattern(format)
//            offsetDateTime.format(formatter)
//        } catch (e: IllegalArgumentException) {
//            // Handle the exception, e.g., log it or return a default value
//            offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) // Fallback to default format
//        }
//    }
//}
/**
 * Use case for formatting an `OffsetDateTime` into a specified format.
 *
 * This use case takes an `OffsetDateTime` and a format string as input and returns
 * a formatted string representation of the date and time.
 *
 * @param offsetDateTime The `OffsetDateTime` to format.
 * @param format The desired format string. Defaults to "yyyy-MM-dd - HH:mm:ss".
 * @return The formatted date and time string.
 *
 * @throws IllegalArgumentException If the provided format string is invalid.
 * for example yyyy/MM/dd - HH:mm:ss "/" is not supported
 */
class FormatDateUseCase @Inject constructor() {
    operator fun invoke(
        offsetDateTime: OffsetDateTime,
        format: String = "yyyy-MM-dd - HH:mm:ss"
    ): String {
        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}