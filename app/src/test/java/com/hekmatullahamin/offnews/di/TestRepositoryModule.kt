package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.data.repository.FakeNewsRepository
import com.hekmatullahamin.offnews.data.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {
    @Provides
    fun bindNewsRepository(
    ): NewsRepository = FakeNewsRepository()
}