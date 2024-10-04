package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.data.network.NewsApiService
import com.hekmatullahamin.offnews.data.network.NewsNetworkDataSource
import com.hekmatullahamin.offnews.data.network.NewsNetworkDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt module for providing network-related dependencies.
 * This module provides the Retrofit instance and the News API service.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Qualifier for the base URL of the News API.
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class BaseUrl

    /**
     * Provides the base URL of the News API.
     * @return The base URL of the News API.
     */
    @Provides
    @Singleton
    @BaseUrl
    fun providesBaseUrl(): String = "https://newsapi.org/v2/"

    /**
     * Provides the Retrofit instance.
     * @return The Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(@BaseUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .build()
    }

    /**
     * Provides the News API service.
     * @param retrofit The Retrofit instance.
     * @return The News API service.
     */
    @Provides
    @Singleton
    fun provideNewsApi(
        retrofit: Retrofit
    ): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}