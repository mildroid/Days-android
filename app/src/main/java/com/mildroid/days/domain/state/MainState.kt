package com.mildroid.days.domain.state

import com.mildroid.days.domain.Event

sealed class MainViewState {

    data class ViewType(val entryTime: EntryTime) : MainViewState()
    data class Data(val events: List<Event>) : MainViewState()
    object EmptyData : MainViewState()
    object Loading : MainViewState()
}

sealed class MainStateEvent {

    object Init: MainStateEvent()
    data class SelectedEvent(val event: Event): MainStateEvent()
    object SaveEvents: MainStateEvent()
}

enum class EntryTime {
    FIRST,
    LAST
}