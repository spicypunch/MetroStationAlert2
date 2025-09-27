package com.jm.metrostationalert.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jm.domain.repository.LocationRepository
import kr.jm.domain.repository.OpenApiRepository
import kr.jm.domain.repository.SubwayStationRepository
import kr.jm.domain.repository.UserPreferencesRepository
import kr.jm.domain.usecase.AddAlertStationUseCase
import kr.jm.domain.usecase.AddBookmarkUseCase
import kr.jm.domain.usecase.GetAddedAlertStationUseCase
import kr.jm.domain.usecase.GetAlertDistanceUseCase
import kr.jm.domain.usecase.GetAlertSettingsUseCase
import kr.jm.domain.usecase.GetAlertStateUseCase
import kr.jm.domain.usecase.GetAlertStationLocationUseCase
import kr.jm.domain.usecase.GetBookmarkUseCase
import kr.jm.domain.usecase.GetNotificationContentUseCase
import kr.jm.domain.usecase.GetNotificationTitleUseCase
import kr.jm.domain.usecase.GetSubwayArrivalTimeUseCase
import kr.jm.domain.usecase.GetSubwayStationsUseCase
import kr.jm.domain.usecase.ReactivateAlertUseCase
import kr.jm.domain.usecase.RemoveBookmarkUseCase
import kr.jm.domain.usecase.SaveAlertDistanceUseCase
import kr.jm.domain.usecase.SaveNotificationSettingsUseCase
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

    @Provides
    @Singleton
    fun provideRemoveBookmarkUseCase(
        repository: UserPreferencesRepository
    ): RemoveBookmarkUseCase {
        return RemoveBookmarkUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddAlertStationUseCase(
        repository: UserPreferencesRepository
    ): AddAlertStationUseCase {
        return AddAlertStationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAddedAlertStationUseCase(
        repository: UserPreferencesRepository
    ): GetAddedAlertStationUseCase {
        return GetAddedAlertStationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSubwayArrivalTimeUseCase(
        repository: OpenApiRepository
    ): GetSubwayArrivalTimeUseCase {
        return GetSubwayArrivalTimeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBookmarkUseCase(
        repository: UserPreferencesRepository
    ): GetBookmarkUseCase {
        return GetBookmarkUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAlertStationLocationUseCase(
        repository: LocationRepository
    ): GetAlertStationLocationUseCase {
        return GetAlertStationLocationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAlertSettingsUseCase(
        repository: LocationRepository
    ): GetAlertSettingsUseCase {
        return GetAlertSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAlertDistanceUseCase(
        repository: UserPreferencesRepository
    ): GetAlertDistanceUseCase {
        return GetAlertDistanceUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetNotificationTitleUseCase(
        repository: UserPreferencesRepository
    ): GetNotificationTitleUseCase {
        return GetNotificationTitleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetNotificationContentUseCase(
        repository: UserPreferencesRepository
    ): GetNotificationContentUseCase {
        return GetNotificationContentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveAlertDistanceUseCase(
        repository: UserPreferencesRepository
    ): SaveAlertDistanceUseCase {
        return SaveAlertDistanceUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveNotificationSettingsUseCase(
        repository: UserPreferencesRepository
    ): SaveNotificationSettingsUseCase {
        return SaveNotificationSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAlertStateUseCase(
        repository: LocationRepository
    ): GetAlertStateUseCase {
        return GetAlertStateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideReactivateAlertUseCase(
        repository: LocationRepository
    ): ReactivateAlertUseCase {
        return ReactivateAlertUseCase(repository)
    }

}