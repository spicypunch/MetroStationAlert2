package com.jm.metrostationalert.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jm.domain.repository.SubwayStationRepository
import kr.jm.domain.repository.UserPreferencesRepository
import kr.jm.domain.usecase.AddBookmarkUseCase
import kr.jm.domain.usecase.GetSubwayStationsUseCase
import kr.jm.domain.usecase.SearchSubwayStationsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetSubwayStationsUseCase(
        repository: SubwayStationRepository
    ): GetSubwayStationsUseCase {
        return GetSubwayStationsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchSubwayStationsUseCase(
        repository: SubwayStationRepository
    ): SearchSubwayStationsUseCase {
        return SearchSubwayStationsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddBookmarkUseCase(
        repository: UserPreferencesRepository
    ): AddBookmarkUseCase {
        return AddBookmarkUseCase(repository)
    }
}