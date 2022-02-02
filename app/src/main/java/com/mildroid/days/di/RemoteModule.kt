package com.mildroid.days.di

import com.mildroid.days.BuildConfig
import com.mildroid.days.Repository
import com.mildroid.days.remote.Api
import com.mildroid.days.utils.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun retrofitProvider(moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_UNSPLASH_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @UnsplashHeaderInterceptor headerInterceptor: Interceptor,
        @UnsplashAccessKeyInterceptor accessKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(accessKeyInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun loggingInterceptorProvider(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @UnsplashAccessKeyInterceptor
    @Provides
    fun accessKeyInterceptor() = Interceptor {
        val authenticatedRequest = it
            .request()
            .newBuilder()
            .addHeader("Authorization", "Client-ID ${BuildConfig.DEV_UNSPLASH_ACCESS_KEY}")
            .build()

        it.proceed(authenticatedRequest)
    }

    @UnsplashHeaderInterceptor
    @Provides
    fun headerInterceptor() = Interceptor {
        val headerRequest = it
            .request()
            .newBuilder()
            .addHeader(CONTENT_TYPE, APPLICATION_JSON)
            .addHeader(ACCEPT_VERSION, "v1")
            .build()

        it.proceed(headerRequest)
    }

    @Singleton
    @Provides
    fun apiProvider(retrofit: Retrofit): Api {
        return retrofit
            .create(Api::class.java)
    }
}