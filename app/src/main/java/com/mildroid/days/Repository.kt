package com.mildroid.days

import com.mildroid.days.db.EventDao
import com.mildroid.days.domain.Event
import com.mildroid.days.remote.Api
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    private val dao: EventDao
) {

    suspend fun photo(id: String) = api.photo(id)

    suspend fun photoList(page: Int) = api.photoList(page)

    suspend fun searchPhotos(query: String, page: Int) = api.searchPhotos(query, page).results

    suspend fun saveEvents(events: List<Event>) = dao.saveEvents(events)

    suspend fun eventById(id: Int) = dao.eventById(id)

    suspend fun saveEvent(event: Event) = dao.saveEvent(event)

    fun events() = dao.events()
}
