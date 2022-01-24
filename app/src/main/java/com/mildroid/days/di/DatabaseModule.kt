package com.mildroid.days.di

import android.content.Context
import androidx.room.Room
import com.mildroid.days.db.AppDatabase
import com.mildroid.days.db.EventDao
import com.mildroid.days.utils.APP_DATABASE_NAME
import com.mildroid.days.utils.PhotoConverter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun databaseProvider(
        @ApplicationContext context: Context,
        photoConverter: PhotoConverter
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .addTypeConverter(photoConverter)
            .build()
    }

    @Singleton
    @Provides
    fun eventDaoProvider(database: AppDatabase): EventDao {
        return database.eventDao()
    }

    @Singleton
    @Provides
    fun photoConverterProvider(moshi: Moshi): PhotoConverter {
        return PhotoConverter(moshi)
    }
}