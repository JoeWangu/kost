package com.saddict.testkost.products.ui.screens.products

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddict.testkost.products.data.remote.remository.ApiRepository
import com.saddict.testkost.products.model.remote.PostProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

sealed interface ProductEntryUiCondition{
    data object Success: ProductEntryUiCondition
    data object Error: ProductEntryUiCondition
    data object Loading: ProductEntryUiCondition
}

@HiltViewModel
class ProductEntryViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    private val _uiCondition = MutableSharedFlow<ProductEntryUiCondition>()
    val uiCondition: SharedFlow<ProductEntryUiCondition> = _uiCondition
    var productEntryUiState by mutableStateOf(ProductEntryUiState())
        private set

    //    private val apiRepo = AppApi(context).productsRepository
//    private val apiRepo = repository

    fun updateUiState(entryDetails: EntryDetails) {
        productEntryUiState =
            ProductEntryUiState(
                entryDetails = entryDetails,
                isEntryValid = validateInput(entryDetails)
            )
    }

    suspend fun saveProduct() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    if (validateInput()) {
                        _uiCondition.emit(ProductEntryUiCondition.Loading)
                        val response = repository.postProducts(productEntryUiState.entryDetails.toPostProducts())
                        if (response.isSuccessful){
                            val responseBody = response.body()
                            Log.d("Success", "$responseBody")
                            _uiCondition.emit(ProductEntryUiCondition.Success)
                        }else{
                            val errorBody = response.raw()
                            Log.e("NotSent", "Error: $errorBody")
                            _uiCondition.emit(ProductEntryUiCondition.Error)
                        }
                    }
                }catch (e: IOException){
                    Log.e("NotSent", "Error: $e")
                }
            }
        }
    }

    private fun validateInput(uiState: EntryDetails = productEntryUiState.entryDetails): Boolean {
        return with(uiState) {
            productName.isNotBlank() && modelNumber.isNotBlank() && specifications.isNotBlank()
                    && price.isNotBlank()
        }
    }
}

data class ProductEntryUiState(
    val entryDetails: EntryDetails = EntryDetails(),
    val isEntryValid: Boolean = false
)

data class EntryDetails(
    val productName: String = "",
    val modelNumber: String = "",
    val specifications: String = "",
    val price: String = "",
    val image: String = "1",
    val category: String = "1",
    val supplier: String = "1"
)

fun EntryDetails.toPostProducts(): PostProduct = PostProduct(
    name = productName,
    model_number = modelNumber,
    specifications = specifications,
    price = price.toDoubleOrNull() ?: 0.0,
    image = image.toIntOrNull() ?: 1,
    category = category.toIntOrNull() ?: 1,
    supplier = supplier.toIntOrNull() ?: 1
)