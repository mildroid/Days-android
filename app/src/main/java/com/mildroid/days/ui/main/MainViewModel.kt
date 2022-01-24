package com.mildroid.days.ui.main

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Event
import com.mildroid.days.utils.APP_ENTRY_STATE
import com.mildroid.days.utils.UpcomingEvents
import com.mildroid.days.utils.dataStore
import com.mildroid.days.utils.log
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

    private val _events = MutableStateFlow(listOf<Event>())
    val events: StateFlow<List<Event>> get() = _events

    private val _viewType = MutableStateFlow(EntryTime.NONE)
    val viewType: StateFlow<EntryTime> get() = _viewType

    val areWeReady = MutableStateFlow(true)

    init {
        viewType()
    }

    private fun viewType() = viewModelScope.launch {
        val context = getApplication<Application>().applicationContext

        val entryKey = booleanPreferencesKey(APP_ENTRY_STATE)
        val isFirstEntry = context.dataStore.data.map {
            it[entryKey] ?: true
        }

        if (isFirstEntry.first()) {
            _viewType.value = EntryTime.FIRST
            upcomingEvents()

            context.dataStore.edit {
                it[entryKey] = false
            }
        } else {
            _viewType.value = EntryTime.LAST
            events()
        }
//        none of these is needed any more!
        this.cancel()
    }

    private fun upcomingEvents() = viewModelScope.launch {
        _events.value = UpcomingEvents.byDaysRemaining()

        delay(500)
        areWeReady.value = false
    }

    private fun events() = viewModelScope.launch {
        val events = repository
            .events()

        _events.value = events.first()

        delay(500)
        areWeReady.value = false
    }

    fun saveEvents(events: List<Event>) = viewModelScope.launch {
        if (events.isNotEmpty()) {
            repository
                .saveEvents(events)

        }
    }
}