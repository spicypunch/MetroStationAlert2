package com.jm.metrostationalert2.di

import com.jm.metrostationalert2.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdModule {

    @Provides
    @Singleton
    @Named("nativeAdUnitId")
    fun provideNativeAdUnitId(): String = BuildConfig.ADMOB_NATIVE_AD_UNIT_ID

    @Provides
    @Singleton
    @Named("bannerAdUnitId")
    fun provideBannerAdUnitId(): String = BuildConfig.ADMOB_BANNER_AD_UNIT_ID
}
