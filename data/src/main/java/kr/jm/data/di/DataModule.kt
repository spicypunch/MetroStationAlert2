package kr.jm.data.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.jm.data.datasource.LocalSubwayStationDataSource
import kr.jm.data.repository.SubwayStationRepositoryImpl
import kr.jm.domain.repository.SubwayStationRepository
import kr.jm.domain.usecase.GetSubwayStationsUseCase
import kr.jm.domain.usecase.SearchSubwayStationsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    @Binds
    @Singleton
    abstract fun bindSubwayStationRepository(
        subwayStationRepositoryImpl: SubwayStationRepositoryImpl
    ): SubwayStationRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    
    @Provides
    @Singleton
    fun provideLocalSubwayStationDataSource(
        @ApplicationContext context: Context
    ): LocalSubwayStationDataSource {
        return LocalSubwayStationDataSource(context)
    }
    
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
}