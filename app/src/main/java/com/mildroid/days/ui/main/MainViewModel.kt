package com.mildroid.days.ui.main

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Event
import com.mildroid.days.domain.state.EntryTime
import com.mildroid.days.domain.state.MainStateEvent
import com.mildroid.days.domain.state.MainViewState
import com.mildroid.days.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository

) : AndroidViewModel(application) {

    private val _viewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState.Loading)
    val viewState: StateFlow<MainViewState> get() = _viewState

    val areWeReady = MutableStateFlow(true)

    private val selectedEvents: MutableList<Event> by lazyOf(mutableListOf())

    private fun viewType() = viewModelScope.launch {
        val context = getApplication<Application>().applicationContext

        val isFirstEntry = context.dataStore.data.map {
            it[booleanPreferencesKey(APP_ENTRY_STATE)] ?: true
        }

        if (isFirstEntry.first()) {
            _viewState.value = MainViewState.ViewType(EntryTime.FIRST)
            upcomingEvents()

            context.dataStore.edit {
                it[booleanPreferencesKey(APP_ENTRY_STATE)] = false
            }
        } else {
            _viewState.value = MainViewState.ViewType(EntryTime.LAST)
            events()
        }
//        none of these is needed any more!
        this.cancel()
    }

    private fun upcomingEvents() = viewModelScope.launch {
        _viewState.value = MainViewState.Data(UpcomingEvents.byDaysRemaining())

        weAreReady()
    }

    private fun events() = viewModelScope.launch {
        val events = repository
            .events()

        events.first().run {
            _viewState.value = if (isNotEmpty())
                MainViewState.Data(this.byDaysRemaining())
            else
                MainViewState.EmptyData
        }

        weAreReady()
    }

    private suspend fun weAreReady() {
        delay(500)
        areWeReady.value = false
    }

    private fun saveEvents(events: List<Event>) = viewModelScope.launch {
        if (events.isNotEmpty()) {
            repository
                .saveEvents(events)

        }
    }

    private fun addSelectedEvent(event: Event) {
        if (selectedEvents.contains(event)) {
            selectedEvents.remove(event)

            if (selectedEvents.isEmpty())
                _viewState.value = MainViewState.EmptyData

        } else {
            selectedEvents.add(event)
        }
    }

    fun onEvent(event: MainStateEvent) {
        when (event) {
            MainStateEvent.Init -> viewType()
            MainStateEvent.SaveEvents -> saveEvents(selectedEvents)
            is MainStateEvent.SelectedEvent -> addSelectedEvent(event.event)
        }
    }
}