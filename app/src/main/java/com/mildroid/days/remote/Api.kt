package com.mildroid.days.remote

import com.mildroid.days.domain.Photo
import com.mildroid.days.domain.response.SearchPhotoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("photos/{id}/")
    suspend fun photo(
        @Path ("id") id: String
    ) : Photo

    @GET("photos/")
    suspend fun photoList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): List<Photo>

    @GET("search/photos")
    suspend fun searchPhotos (
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): SearchPhotoResponse
}