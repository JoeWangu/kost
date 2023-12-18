package com.saddict.testkost.products.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saddict.testkost.products.data.local.AppDatabase
import com.saddict.testkost.products.data.manager.CustomPagingSource
import com.saddict.testkost.products.data.remote.remository.ApiRepository
import com.saddict.testkost.products.model.local.ProductEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {
    @Provides
    @Singleton
    fun providePager(
        productApi: ApiRepository,
        appDatabase: AppDatabase
    ): Pager<Int, ProductEntity>{
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1),
            pagingSourceFactory = { CustomPagingSource(productApi, appDatabase) }
        )
    }
}