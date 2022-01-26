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

    init {
        photos()
    }

    private val _photos = MutableStateFlow<List<Photo>>(listOf())
    val photos: StateFlow<List<Photo>> get() = _photos

    private fun photos() = viewModelScope.launch {
        "photo req sent".log()
        val result = repository
            .photoList()

        result.size.log("result")
        _photos.value = result
    }

}