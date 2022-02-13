package com.mildroid.days.ui.event

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Event
import com.mildroid.days.domain.EventType
import com.mildroid.days.domain.Photo
import com.mildroid.days.utils.*
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

) : AndroidViewModel(application) {

    init {
        events()
    }

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
    }

    private fun events() = viewModelScope.launch {
        repository
            .events()
            .collect {
                it.forEach { event ->
                    event.log()
                }
            }
    }

    fun onEvent(event: EventStateEvent) {
        when (event) {
            is EventStateEvent.Init -> {
                if (event.eventId != null) loadEvent(event.eventId)
                else loadEvent()
            }

            is EventStateEvent.ChangeViewType -> _viewState.value =
                if (event.type == EventViewType.PREVIEW)
                    EventViewState.ViewType(event.type, this.event)

                else EventViewState.ViewType(event.type)

            is EventStateEvent.SaveEvent -> viewModelScope.launch {
                repository.saveEvent(this@EventViewModel.event)
            }
        }
    }
}

sealed class EventViewState {

    object IDLE : EventViewState()
    data class ViewType(val viewType: EventViewType, val event: Event? = null) : EventViewState()
}

sealed class EventStateEvent {

    data class Init(val eventId: Int?) : EventStateEvent()
    data class ChangeViewType(val type: EventViewType) : EventStateEvent()
    object SaveEvent : EventStateEvent()
}