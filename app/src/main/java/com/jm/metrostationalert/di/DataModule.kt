package com.jm.metrostationalert.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.jm.data.datastore.UserPreferencesDataStore
import kr.jm.data.repository.UserPreferencesRepositoryImpl
import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

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
    }
}