package com.mildroid.days.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnsplashAccessKeyInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnsplashHeaderInterceptor