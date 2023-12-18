package com.saddict.testkost.products.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.saddict.testkost.products.model.local.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao{
    @Upsert
    suspend fun upsertAllProducts(products: List<ProductEntity>)
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun fetchAllProducts(): Flow<List<ProductEntity>>
    @Query("SELECT * from products ORDER BY id DESC")
    fun fetchAllProductsDesc(): Flow<List<ProductEntity>>
    @Query("SELECT * FROM products WHERE id = :id")
    fun fetchOneProduct(id: Int): Flow<ProductEntity>
    @Query("SELECT * FROM products ORDER BY id ASC")
    suspend fun getAllPaged(): List<ProductEntity>
}