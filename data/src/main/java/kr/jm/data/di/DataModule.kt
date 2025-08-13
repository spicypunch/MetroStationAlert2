package kr.jm.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jm.data.repository.SubwayStationRepositoryImpl
import kr.jm.domain.repository.SubwayStationRepository
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