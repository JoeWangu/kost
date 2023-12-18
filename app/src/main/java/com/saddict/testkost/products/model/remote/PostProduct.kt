package com.saddict.testkost.products.model.remote

data class PostProduct(
    val name: String,
    @Suppress("PropertyName")
    val model_number: String,
    val specifications: String,
    val price: Double,
    val image: Int,
    val category: Int,
    val supplier: Int,
)
