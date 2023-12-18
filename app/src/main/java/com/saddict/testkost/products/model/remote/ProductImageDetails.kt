package com.saddict.testkost.products.model.remote

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty

@Keep
data class ProductImageDetails(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("image")
    val image: String
)
