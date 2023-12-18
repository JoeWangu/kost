package com.saddict.testkost.products.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saddict.testkost.products.model.local.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}