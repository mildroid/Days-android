package com.mildroid.days

import com.mildroid.days.remote.Api
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api

) {

    suspend fun photo(id: String) = api.photo(id)

    suspend fun photoList() = api.photoList()
}
