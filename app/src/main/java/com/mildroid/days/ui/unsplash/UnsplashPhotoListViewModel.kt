package com.mildroid.days.ui.unsplash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Photo
import com.mildroid.days.domain.state.UnsplashPhotoListStateEvent
import com.mildroid.days.domain.state.UnsplashPhotoListViewState
import com.mildroid.days.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnsplashPhotoListViewModel @Inject constructor(
    private val repository: Repository

) : ViewModel() {

    private val _viewState: MutableStateFlow<UnsplashPhotoListViewState> =
        MutableStateFlow(UnsplashPhotoListViewState.Loading)

    val viewState: StateFlow<UnsplashPhotoListViewState> get() = _viewState

    private var lastQuery: String? = null

    private fun photos(page: Int, query: String?) = viewModelScope.launch {
        if (query != lastQuery || query == null) {
            _viewState.value = UnsplashPhotoListViewState.Reset

            lastQuery = query

            val result: List<Photo> = if (query == null || query == "") {
                repository
                    .photoList(page)

            } else {
                lastQuery = query

                repository
                    .searchPhotos(query, page)
            }

            _viewState.value = UnsplashPhotoListViewState.Data(result)
        }

    }

    fun onEvent(event: UnsplashPhotoListStateEvent) {
        when (event) {
            is UnsplashPhotoListStateEvent.Fetch -> photos(event.page, event.query?.trim())
        }
    }

}