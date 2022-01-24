package com.mildroid.days.di

import com.mildroid.days.Repository
import com.mildroid.days.db.EventDao
import com.mildroid.days.remote.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun repositoryProvider(api: Api, dao: EventDao): Repository {
        return Repository(api, dao)
    }
}