package com.hekmatullahamin.offnews.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * A converter class for converting between [OffsetDateTime] and [String] representations.
 *
 * This class provides two type converters for Room:
 * - `fromTimestamp`: Converts a string representation of an OffsetDateTime to an OffsetDateTime object.
 * - `dateToTimestamp`: Converts an OffsetDateTime object to a string representation.
 */
object OffsetDateTimeConverter {

    /**
     * Converts a string representation of an OffsetDateTime to an OffsetDateTime object.
     *
     * @param value The string representation of the OffsetDateTime.
     * @return The OffsetDateTime object, or null if the input is null or invalid.
     */
    @TypeConverter
    fun fromTimestamp(value: String?): OffsetDateTime? {
        return value?.let {
            OffsetDateTime.parse(it)
        }
    }

    /**
     * Converts an OffsetDateTime object to a string representation.
     *
     * @param date The OffsetDateTime object.
     * @return The string representation of the OffsetDateTime, or null if the input is null.
     */
    @TypeConverter
    fun dateToTimestamp(date: OffsetDateTime?): String? {
        return date?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}