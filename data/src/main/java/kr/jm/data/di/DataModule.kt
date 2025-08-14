package kr.jm.data.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.jm.data.datastore.UserPreferencesDataStore
import kr.jm.data.datasource.LocalSubwayStationDataSource
import kr.jm.data.repository.SubwayStationRepositoryImpl
import kr.jm.data.repository.UserPreferencesRepositoryImpl
import kr.jm.domain.repository.SubwayStationRepository
import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    @Binds
    @Singleton
    abstract fun bindSubwayStationRepository(
        subwayStationRepositoryImpl: SubwayStationRepositoryImpl
    ): SubwayStationRepository
    
    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserPreferencesDataStore(
            @ApplicationContext context: Context
        ): UserPreferencesDataStore {
            return UserPreferencesDataStore(context)
        }

        @Provides
        @Singleton
        fun provideLocalSubwayStationDataSource(
            @ApplicationContext context: Context
        ): LocalSubwayStationDataSource {
            return LocalSubwayStationDataSource(context)
        }
    }
}