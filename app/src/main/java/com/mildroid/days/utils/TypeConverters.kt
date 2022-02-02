package com.mildroid.days.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mildroid.days.domain.Photo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class DateConverter {

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun fromString(date: String): LocalDate {
        return LocalDate.parse(date)
    }
}

@ProvidedTypeConverter
class PhotoConverter @Inject constructor(
    private val photoJsonAdapter: JsonAdapter<Photo>
) {

    @TypeConverter
    fun fromJson(photo: String): Photo? {
        return photoJsonAdapter.fromJson(photo)
    }

    @TypeConverter
    fun fromPhoto(photo: Photo?): String {
        return photoJsonAdapter.toJson(photo)
    }
}