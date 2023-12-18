package com.saddict.testkost.products.di

import com.saddict.testkost.products.data.manager.PreferenceDataStore
import com.saddict.testkost.products.data.manager.PreferenceDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun providePreferences(preferenceDataStoreImpl: PreferenceDataStoreImpl): PreferenceDataStore{
        return preferenceDataStoreImpl
    }
}