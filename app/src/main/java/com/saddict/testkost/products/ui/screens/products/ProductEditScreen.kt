package com.saddict.testkost.products.ui.screens.products

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.saddict.testkost.R
import com.saddict.testkost.products.ui.navigation.NavigationDestination
import com.saddict.testkost.utils.toastUtil
import com.saddict.testkost.utils.utilscreens.KostTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ProductEditDestination : NavigationDestination {
    override val route: String = "product_edit"
    override val titleRes: Int = R.string.product_edit_title
    const val productIdArg = "productId"
    val routeWithArgs = "$route/{$productIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: ProductEditViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            KostTopAppBar(
                title = stringResource(id = ProductEditDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ProductEntryBody(
            productEntryUiState = viewModel.productEditUiState,
            onProductValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateProduct()
                    viewModel.uiCondition.collect { state ->
                        when (state) {
                            ProductUpdateUiCondition.Error -> {
                                ctx.toastUtil("Could not save")
                                navigateBack()
                            }

                            ProductUpdateUiCondition.Loading -> ctx.toastUtil("Saving Product")
                            is ProductUpdateUiCondition.Success -> {
                                ctx.toastUtil("Updated Successfully")
                                delay(2_000L)
                                navigateBack()
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}