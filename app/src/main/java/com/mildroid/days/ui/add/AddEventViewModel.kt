package com.mildroid.days.ui.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(

) : ViewModel() {

    private val _desireDate: MutableStateFlow<LocalDate> =
        MutableStateFlow(java.time.LocalDate.now().toKotlinLocalDate())

    val desireDate: StateFlow<LocalDate> get() = _desireDate

    fun onDatePickerSpanned(date: LocalDate) {
        _desireDate.value = date
    }
}