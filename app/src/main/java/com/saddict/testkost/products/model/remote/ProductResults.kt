package com.saddict.testkost.products.model.remote

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty

@Keep
data class ProductResults(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val productName: String,
    @JsonProperty("model_number")
    val modelNumber: String,
    @JsonProperty("specifications")
    val specifications: String,
    @JsonProperty("price")
    val price: String,
    @JsonProperty("image")
    val image: Int,
    @JsonProperty("image_detail")
    val imageDetails: ProductImageDetails,
    @JsonProperty("category")
    val category: Int,
    @JsonProperty("supplier")
    val supplier: Int
)
