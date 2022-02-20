package com.mathandcoffee.cscodetest.http

import com.mathandcoffee.cscodetest.rest.AuthenticationAPIService
import com.mathandcoffee.cscodetest.rest.ProductAPIService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitProvider {
    private const val baseUrl = "https://cscodetest.herokuapp.com/api/"

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationAPIService(retrofit: Retrofit): AuthenticationAPIService {
        return retrofit.create(AuthenticationAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductsAPIService(retrofit: Retrofit): ProductAPIService {
        return retrofit.create(ProductAPIService::class.java)
    }
}