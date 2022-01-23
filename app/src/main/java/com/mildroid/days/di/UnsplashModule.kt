package com.mildroid.days.di

import com.mildroid.days.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UnsplashModule {

    @Provides
    fun accessKey() = BuildConfig.DEV_UNSPLASH_ACCESS_KEY

}