package com.mildroid.days.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "events")
data class Event(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = title,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "image")
    val image: String?,

    @ColumnInfo(name = "type")
    val type: EventType,

    @ColumnInfo(name = "photo")
    val photo: Photo? = null
)

enum class EventType {
    OFFICIAL,
    PUBLIC,
    NONPUBLIC,
    CUSTOM
}