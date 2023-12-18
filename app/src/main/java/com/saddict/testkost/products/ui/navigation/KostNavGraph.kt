package com.saddict.testkost.products.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saddict.testkost.products.data.manager.PreferenceDataStoreImpl
import com.saddict.testkost.products.ui.screens.home.HomeDestination
import com.saddict.testkost.products.ui.screens.home.HomeScreen
import com.saddict.testkost.products.ui.screens.products.ProductDetailsDestination
import com.saddict.testkost.products.ui.screens.products.ProductDetailsScreen
import com.saddict.testkost.products.ui.screens.products.ProductEditDestination
import com.saddict.testkost.products.ui.screens.products.ProductEditScreen
import com.saddict.testkost.products.ui.screens.products.ProductEntryDestination
import com.saddict.testkost.products.ui.screens.products.ProductEntryScreen
import com.saddict.testkost.products.ui.screens.register.LoginDestination
import com.saddict.testkost.products.ui.screens.register.LoginScreen
import com.saddict.testkost.products.ui.screens.register.RegisterDestination
import com.saddict.testkost.products.ui.screens.register.RegisterScreen
import com.saddict.testkost.utils.toastUtil
import com.saddict.testkost.utils.utilscreens.LoadingDestination
import com.saddict.testkost.utils.utilscreens.LoadingScreen

@Composable
fun KostNavGraph() {
    val navController = rememberNavController()
    var pressedTime: Long = 0
    val ctx = LocalContext.current
    val preference = PreferenceDataStoreImpl(ctx)
    val activity = LocalContext.current as? Activity
    LaunchedEffect(key1 = Unit) {
        val token = preference.getToken()
        if (token.isNotBlank()) {
            navController.navigate(HomeDestination.route) {
                popUpTo(LoadingDestination.route) { inclusive = true }
            }
        } else {
            navController.navigate(LoginDestination.route) {
                popUpTo(LoadingDestination.route) { inclusive = true }
            }
        }
    }
    val tokenLot = preference.getToken()
    NavHost(
        navController = navController,
        startDestination = if (tokenLot == "") LoginDestination.route else HomeDestination.route,
        modifier = Modifier
    ) {
        composable(route = LoadingDestination.route) {
            LoadingScreen()
        }
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToRegister = { navController.navigate(RegisterDestination.route) }
            )
        }
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = HomeDestination.route) {
            BackHandler {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    activity?.finish()
                } else {
                    ctx.toastUtil("Press back again to exit")
                }
                pressedTime = System.currentTimeMillis()
            }
            HomeScreen(
                navigateToItemDetails = { navController.navigate("${ProductDetailsDestination.route}/${it}") },
                navigateToItemEntry = { navController.navigate(ProductEntryDestination.route) },
            )
        }
            composable(
                route = ProductDetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(ProductDetailsDestination.productIdArg) {
                    type = NavType.IntType
                })
            ) {
                ProductDetailsScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToEditProduct = { navController.navigate("${ProductEditDestination.route}/${it}") }
                )
            }
            composable(route = ProductEntryDestination.route) {
                ProductEntryScreen(
                    navigateBack = { navController.popBackStack() },
                )
            }
            composable(
                route = ProductEditDestination.routeWithArgs,
                arguments = listOf(navArgument(ProductEditDestination.productIdArg) {
                    type = NavType.IntType
                })
            ) {
                ProductEditScreen(
                    navigateBack = { navController.popBackStack() }
                )
            }
    }
}