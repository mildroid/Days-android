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

    suspend fun photoList() = api.photoList()

    suspend fun saveEvents(events: List<Event>) = dao.saveEvents(events)

    suspend fun events() = dao.events()
}
