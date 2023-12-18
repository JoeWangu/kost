package com.saddict.testkost.products.di

import android.content.Context
import androidx.room.Room
import com.saddict.testkost.products.data.local.AppDatabase
import com.saddict.testkost.products.data.local.ProductDao
import com.saddict.testkost.products.data.local.locasitory.LocalDataSource
import com.saddict.testkost.products.data.local.locasitory.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .build()
    }
    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }
    @Provides
    @Singleton
    fun provideLocalDataSource(productDao: ProductDao): LocalDataSource{
        return LocalDataSourceImpl(productDao)
    }
}