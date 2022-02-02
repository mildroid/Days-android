package com.mildroid.days.ui.event

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Event
import com.mildroid.days.domain.EventType
import com.mildroid.days.domain.Photo
import com.mildroid.days.utils.TEMPORARY_EVENT_DATE
import com.mildroid.days.utils.TEMPORARY_EVENT_PHOTO
import com.mildroid.days.utils.TEMPORARY_EVENT_TITLE
import com.mildroid.days.utils.tempDataStore
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDate
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val photoJsonAdapter: JsonAdapter<Photo>

    ): AndroidViewModel(application) {

    private val _viewState: MutableStateFlow<EventViewState> =
        MutableStateFlow(EventViewState.IDLE)

    val viewState: StateFlow<EventViewState> get() = _viewState

    private lateinit var event: Event

    private fun loadEvent(id: Int) = viewModelScope.launch {
        event = repository
            .eventById(id)
    }

    private fun loadEvent() = viewModelScope.launch {
        val temp = getApplication<Application>()
            .applicationContext
            .tempDataStore
            .data.first()

        val title = temp[stringPreferencesKey(TEMPORARY_EVENT_TITLE)]!!
        val date = temp[stringPreferencesKey(TEMPORARY_EVENT_DATE)]!!

        event = Event(
            title = title,
            date = date.toLocalDate(),
            type = EventType.CUSTOM,
            image = null,
            photo = photoJsonAdapter.fromJson(temp[stringPreferencesKey(TEMPORARY_EVENT_PHOTO)]!!)
        )

        _viewState.value = EventViewState.EventDetails(title, date)
    }

    fun onEvent(event: EventStateEvent) {
        when (event) {
            is EventStateEvent.Initial -> {
                if (event.eventId != null) loadEvent(event.eventId)
                else loadEvent()
            }
            is EventStateEvent.ViewType -> _viewState.value =
                EventViewState.ViewTypeChange(event.type)
            is EventStateEvent.SaveEvent -> viewModelScope.launch {
                repository.saveEvent(this@EventViewModel.event)
            }
        }
    }
}

sealed class EventViewState {

    object IDLE: EventViewState()
    data class EventDetails(val title: String, val date: String): EventViewState()
    data class ViewTypeChange(val viewType: EventViewType): EventViewState()
}

sealed class EventStateEvent {

    data class Initial(val eventId: Int?): EventStateEvent()
    data class ViewType(val type: EventViewType): EventStateEvent()
    object SaveEvent: EventStateEvent()
}