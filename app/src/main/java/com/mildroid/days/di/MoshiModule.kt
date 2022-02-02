package com.mildroid.days.di

import com.mildroid.days.domain.Photo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule {

    @Singleton
    @Provides
    fun moshiProvider(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun photoJsonAdapterProvider(moshi: Moshi): JsonAdapter<Photo> {
        return moshi.adapter(Photo::class.java)
    }
}