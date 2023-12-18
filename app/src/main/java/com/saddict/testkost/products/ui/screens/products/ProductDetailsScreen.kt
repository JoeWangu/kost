package com.saddict.testkost.products.ui.screens.products

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.saddict.testkost.R
import com.saddict.testkost.products.model.local.ProductEntity
import com.saddict.testkost.products.ui.navigation.NavigationDestination
import com.saddict.testkost.utils.utilscreens.KostTopAppBar

object ProductDetailsDestination : NavigationDestination {
    override val route = "product_details"
    override val titleRes = R.string.product_details
    const val productIdArg = "itemId"
    val routeWithArgs = "$route/{$productIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navigateToEditProduct: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState = productDetailsViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            KostTopAppBar(
                title = stringResource(ProductDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
            )
        },floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditProduct(uiState.value.productDetails.id) },
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.product_edit_title),
                )
            }
        },
    ) { innerPadding ->
        ProductDetailBody(
            productDetailsUiState = uiState.value,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ProductDetailBody(
    productDetailsUiState: ProductDetailsUiState,
    modifier: Modifier = Modifier
) {
    ProductDetailItems(
        productEntity = productDetailsUiState.productDetails.toProductEntity(),
        modifier = modifier
    )
}

@Composable
fun ProductDetailItems(
    productEntity: ProductEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ProductDetailsRow(
                labelResID = R.string.id,
                productDetail = productEntity.id.toString()
            )
            ProductDetailsRow(
                labelResID = R.string.name,
                productDetail = productEntity.productName
            )
            ProductDetailsRow(
                labelResID = R.string.model_number,
                productDetail = productEntity.modelNumber
            )
            ProductDetailsRow(
                labelResID = R.string.specifications,
                productDetail = productEntity.specifications
            )
            ProductDetailsRow(
                labelResID = R.string.price,
                productDetail = productEntity.price
            )
            ProductDetailsRow(
                labelResID = R.string.image,
                productDetail = productEntity.image.toString()
            )
            ProductDetailsRow(
                labelResID = R.string.category,
                productDetail = productEntity.category.toString()
            )
            ProductDetailsRow(
                labelResID = R.string.supplier,
                productDetail = productEntity.supplier.toString()
            )
        }
    }
}

@Composable
private fun ProductDetailsRow(
    @StringRes labelResID: Int,
    productDetail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Text(
            text = productDetail,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_large))
        )
    }
}