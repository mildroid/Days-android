package com.mildroid.days.remote

import com.mildroid.days.domain.Photo
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("photos/{id}/")
    suspend fun photo(
        @Path ("id") id: String
    ) : Photo

    @GET("photos/")
    suspend fun photoList(): List<Photo>
}