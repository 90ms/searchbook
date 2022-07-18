package com.a90ms.searchbook.di

import com.a90ms.searchbook.network.GoogleApisService
import com.a90ms.searchbook.repository.GoogleServiceRepository
import com.a90ms.searchbook.repository.GoogleServiceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGoogleServiceRepository(googleApisService: GoogleApisService):
            GoogleServiceRepository = GoogleServiceRepositoryImpl(googleApisService)
}