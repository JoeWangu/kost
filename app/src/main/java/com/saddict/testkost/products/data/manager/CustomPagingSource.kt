package com.saddict.testkost.products.data.manager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saddict.testkost.products.data.local.AppDatabase
import com.saddict.testkost.products.data.remote.remository.ApiRepository
import com.saddict.testkost.products.model.local.ProductEntity
import com.saddict.testkost.utils.Constants.INITIAL_PAGE
import com.saddict.testkost.utils.DataMapper.Companion.mapToEntity
import javax.inject.Inject

class CustomPagingSource @Inject constructor(
    private val productApi: ApiRepository,
    private val appDatabase: AppDatabase
): PagingSource<Int, ProductEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ProductEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductEntity> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = productApi.getProducts(page).results
            val entities = response.map { it.mapToEntity() }
            appDatabase.productDao().upsertAllProducts(entities)
            val dataLoaded = appDatabase.productDao().getAllPaged()
            LoadResult.Page(
                data = dataLoaded,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}