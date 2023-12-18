package com.saddict.testkost.products.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.saddict.testkost.R
import com.saddict.testkost.products.model.remote.ProductResults
import com.saddict.testkost.products.ui.navigation.NavigationDestination
import com.saddict.testkost.utils.toastUtilLong
import com.saddict.testkost.utils.utilscreens.KostBottomNavigationBar
import com.saddict.testkost.utils.utilscreens.KostTopAppBar

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home
}

@Composable
fun HomeScreen(
//    navigateToLogin: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    products: LazyPagingItems<ProductResults> = viewModel.productPagingFlow.collectAsLazyPagingItems(),
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = products.loadState) {
        if (products.loadState.refresh is LoadState.Error) {
            context.toastUtilLong("Error: " + (products.loadState.refresh as LoadState.Error).error.message)
        }
    }
    Box(modifier = modifier) {
        if (products.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            HomeBase(
//                navigateToLogin = navigateToLogin,
                navigateToItemEntry = navigateToItemEntry,
                navigateToItemDetails = navigateToItemDetails,
//                refreshAction = viewModel::deleteAll,
                products = products
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBase(
    navigateToItemEntry: () -> Unit,
    navigateToItemDetails: (Int) -> Unit,
    products: LazyPagingItems<ProductResults>,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            KostTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.product_entry_title)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            KostBottomNavigationBar(
                navigateToHome = { /*TODO*/ },
                navigateToFavourites = { /*TODO*/ },
                navigateToTrending = { /*TODO*/ },
                navigateToAccount = { /*TODO*/ })
        }
    ) { innerPadding ->
        HomeBody(
            products = products,
            onProductClick = navigateToItemDetails,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun HomeBody(
    products: LazyPagingItems<ProductResults>,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ProductScreen(
        products = products,
        onProductClick = { onProductClick(it.id) },
        modifier = modifier
    )
}

@Composable
fun ProductScreen(
    products: LazyPagingItems<ProductResults>,
    onProductClick: (ProductResults) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState
    ) {
        items(products.itemCount, key = products.itemKey { it.id }) { index ->
            val product = products[index]
            product.let {
                if (it != null) {
                    ProductCard(
                        product = it,
                        modifier = Modifier
                            .padding()
                            .clickable { onProductClick(product!!) }
                    )
                } else {
                    Text(text = "Item not found")
                }
            }
        }
        item {
            if (products.loadState.append is LoadState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductResults,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier)
        {
            Card(
                modifier = Modifier,
                shape = MaterialTheme.shapes.small,
            ) {
                Box(modifier = Modifier) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(product.imageDetails.image)
                            .crossfade(true).build(),
                        error = painterResource(R.drawable.ic_broken_image),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = stringResource(R.string.p_image),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(width = 0.dp, height = 250.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(30.dp)
                            .align(Alignment.TopEnd)
                            .clickable {}
                    )
                }
            }
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.padding_small),
                            start = dimensionResource(id = R.dimen.padding_medium),
                        )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.padding_small),
                            end = dimensionResource(id = R.dimen.padding_medium),
                        )
                )
            }
            Row(modifier = Modifier) {
                Text(
                    text = product.specifications,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.padding_small),
                            start = dimensionResource(id = R.dimen.padding_medium),
                        )
                )
            }
        }
    }
}