package com.mildroid.days.ui.unsplash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mildroid.days.Repository
import com.mildroid.days.domain.Photo
import com.mildroid.days.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnsplashPhotoListViewHolder @Inject constructor(
    private val repository: Repository

) : ViewModel() {

    private val _viewState: MutableStateFlow<UnsplashPhotoListViewState> =
        MutableStateFlow(UnsplashPhotoListViewState.Loading)

    val viewState: StateFlow<UnsplashPhotoListViewState> get() = _viewState

    private fun photos(page: Int) = viewModelScope.launch {
        val result = repository
            .photoList(page)

        _viewState.value = UnsplashPhotoListViewState.Data(result)
    }

    fun onEvent(event: UnsplashPhotoListStateEvent) {
        when (event) {
            is UnsplashPhotoListStateEvent.Fetch -> photos(event.page)
        }
    }

}

sealed class UnsplashPhotoListViewState {

    object Loading : UnsplashPhotoListViewState()
    data class Data(val photos: List<Photo>?) : UnsplashPhotoListViewState()
    data class Error(val t: Throwable) : UnsplashPhotoListViewState()
}

sealed class UnsplashPhotoListStateEvent {

    data class Fetch(
        val page: Int = 1,
        val query: String? = null
    ) : UnsplashPhotoListStateEvent()
}