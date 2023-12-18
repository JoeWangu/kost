package com.saddict.testkost.products.data.local.locasitory

import com.saddict.testkost.products.model.local.ProductEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun upsertAllProducts(products: List<ProductEntity>)
    suspend fun fetchAllProducts(): Flow<List<ProductEntity>>
    fun fetchAllProductsDesc(): Flow<List<ProductEntity>>
    fun fetchOneProduct(id: Int): Flow<ProductEntity>
    //    suspend fun getFavourites(): Flow<List<ProductFavourites>>
}