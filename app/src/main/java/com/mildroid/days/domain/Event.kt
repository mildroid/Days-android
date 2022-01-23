package com.mildroid.days.domain

import kotlinx.datetime.LocalDate

data class Event(

    val id: Int = 0,
    val title: String,
    val des: String = title,
    val date: LocalDate,
    val image: String?,
    val type: EventType,
    val photo: Photo? = null
)

enum class EventType {
    OFFICIAL,
    PUBLIC,
    NONPUBLIC,
    CUSTOM
}