package com.mildroid.days.domain.state

import com.mildroid.days.domain.Photo

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