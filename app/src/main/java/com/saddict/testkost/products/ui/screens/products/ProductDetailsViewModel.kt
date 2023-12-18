package com.saddict.testkost.products.ui.screens.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddict.testkost.products.data.usecases.GetProductUseCase
import com.saddict.testkost.products.model.local.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProductDetails(
    val id: Int = 0,
    val productName: String? = "",
    val modelNumber: String? = "",
    val specifications: String? = "",
    val price: String? = "",
    val image: Int? = 0,
    val imageUrl: String? = "",
    val category: String? = "",
    val supplier: String? = "",
)

data class ProductDetailsUiState(
    val productDetails: ProductDetails = ProductDetails()
)

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getProductUseCase: GetProductUseCase
): ViewModel() {
    private val productId: Int = checkNotNull(savedStateHandle[ProductDetailsDestination.productIdArg])
    val uiState: StateFlow<ProductDetailsUiState> =
        getProductUseCase.getProduct(productId)
            .filterNotNull()
            .map { ProductDetailsUiState(productDetails = it.toProductDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProductDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

fun ProductEntity.toProductDetails(): ProductDetails = ProductDetails(
    id = id,
    productName = productName,
    modelNumber = modelNumber,
    specifications = specifications,
    price = price,
    image = image,
    imageUrl = imageDetails,
    category = category.toString(),
    supplier = supplier.toString()
)

fun ProductDetails.toProductEntity(): ProductEntity = ProductEntity(
    id = id,
    productName = productName ?: "",
    modelNumber = modelNumber ?: "",
    specifications = specifications ?: "",
    price = price ?: "",
    image = image ?: 1,
    category = category?.toIntOrNull() ?: 0,
    supplier = supplier?.toIntOrNull() ?: 0,
    imageDetails = imageUrl ?: ""
)