package com.saddict.testkost.products.data.local.locasitory

import com.saddict.testkost.products.data.local.ProductDao
import com.saddict.testkost.products.model.local.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
): LocalDataSource {
    override suspend fun upsertAllProducts(products: List<ProductEntity>) {
        return productDao.upsertAllProducts(products)
    }

    override suspend fun fetchAllProducts(): Flow<List<ProductEntity>> {
        return productDao.fetchAllProducts()
    }

    override fun fetchAllProductsDesc(): Flow<List<ProductEntity>> {
        return productDao.fetchAllProductsDesc()
    }

    override fun fetchOneProduct(id: Int): Flow<ProductEntity> {
        return productDao.fetchOneProduct(id)
    }
}