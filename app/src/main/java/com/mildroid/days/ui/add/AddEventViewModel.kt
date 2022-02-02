package com.mildroid.days.ui.add

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    application: Application

) : AndroidViewModel(application) {

    private val _viewState: MutableStateFlow<AddEventViewState> =
        MutableStateFlow(AddEventViewState.IDLE)

    val viewState: StateFlow<AddEventViewState>
        get() = _viewState

    private fun onDatePickerSpanned(date: LocalDate) {
        _viewState.value = AddEventViewState.DesireDate(date)
    }

    private fun saveTemporaryDetails(title: String) = viewModelScope.launch {
        getApplication<Application>()
            .applicationContext
            .tempDataStore
            .edit {
                it.clear()
                it[stringPreferencesKey(TEMPORARY_EVENT_TITLE)] = title
                it[stringPreferencesKey(TEMPORARY_EVENT_DATE)] =
                    (viewState.value as AddEventViewState.DesireDate).date.toString()
            }
    }

    fun onEvent(event: AddEventStateEvent) {
        when (event) {
            is AddEventStateEvent.DatePickerSpanned -> onDatePickerSpanned(event.date)
            is AddEventStateEvent.SaveTemporaryData -> saveTemporaryDetails(event.title)
        }
    }
}

sealed class AddEventViewState {

    object IDLE : AddEventViewState()
    data class DesireDate(val date: LocalDate) : AddEventViewState()
}

sealed class AddEventStateEvent {

    data class DatePickerSpanned(val date: LocalDate) : AddEventStateEvent()
    data class SaveTemporaryData(val title: String) : AddEventStateEvent()
}