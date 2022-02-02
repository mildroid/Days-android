package com.mildroid.days.ui.event

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Event
import com.mildroid.days.utils.TEMPORARY_EVENT_DATE
import com.mildroid.days.utils.TEMPORARY_EVENT_TITLE
import com.mildroid.days.utils.log
import com.mildroid.days.utils.tempDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    application: Application,
    private val repository: Repository

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
        val tempEvent = getApplication<Application>()
            .applicationContext
            .tempDataStore
            .data.first()

        _viewState.value = EventViewState.EventDetails(
            tempEvent[stringPreferencesKey(TEMPORARY_EVENT_TITLE)]!!,
            tempEvent[stringPreferencesKey(TEMPORARY_EVENT_DATE)]!!
        )
    }

    fun onEvent(event: EventStateEvent) {
        when (event) {
            is EventStateEvent.Initial -> {
                if (event.eventId != null) loadEvent(event.eventId)
                else loadEvent()
            }
        }
    }
}

sealed class EventViewState {

    object IDLE: EventViewState()
    data class EventDetails(val title: String, val date: String): EventViewState()
}

sealed class EventStateEvent {

    data class Initial(val eventId: Int?): EventStateEvent()
}