package com.saddict.testkost.products.model.remote

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty

@Keep
data class Product(
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("next")
    val next: String?,
    @JsonProperty("previous")
    val previous: String?,
    @JsonProperty("results")
    val results: List<ProductResults>
)
