package com.mildroid.days.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mildroid.days.domain.Event
import com.mildroid.days.utils.DateConverter
import com.mildroid.days.utils.PhotoConverter

@Database(
    entities = [
        Event::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class, PhotoConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}