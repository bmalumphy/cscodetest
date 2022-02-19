package com.mathandcoffee.cscodetest.http

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HTTPClientProvider {

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val cacheDir = File(appContext.cacheDir, "math-and-coffee_http_cache")
        val cacheSize = (50L * 1024 * 1024) // 50 MB
        val bodyLogging = HttpLoggingInterceptor()
        bodyLogging.level = (HttpLoggingInterceptor.Level.BODY)
        val headersLogging = HttpLoggingInterceptor()
        headersLogging.level = (HttpLoggingInterceptor.Level.HEADERS)
        return OkHttpClient
            .Builder()
            .addInterceptor(bodyLogging)
            .addInterceptor(headersLogging)
            .cache(Cache(cacheDir, cacheSize))
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshiInstance(): Moshi {
        return Moshi.Builder().build()
    }
}