package com.saddict.testkost.products.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.saddict.testkost.R
import com.saddict.testkost.products.data.manager.AppUiState
import com.saddict.testkost.products.ui.navigation.NavigationDestination
import com.saddict.testkost.products.ui.screens.home.HomeDestination
import com.saddict.testkost.utils.toastUtil
import com.saddict.testkost.utils.utilscreens.KostTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object RegisterDestination: NavigationDestination {
    override val route = "register"
    override val titleRes = R.string.register
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KostTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        RegisterBody(
            navigateToHome = navigateToHome,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun RegisterBody(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.note_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            RegisterInput(
                navigateToHome = navigateToHome
            )
        }
    }
}

@Composable
fun RegisterInput(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.padding_small)),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Column(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            FieldTextOutlined(
                value = username,
                onValueChange = { username = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = Icons.Filled.AccountCircle,
                label = R.string.username
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
            FieldTextOutlined(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = Icons.Filled.Email,
                label = R.string.email
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
            FieldTextOutlined(
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = Icons.Filled.Lock,
                label = R.string.password
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.register(username = username, email = email, password = password)
                        viewModel.uiState.collect { state ->
                            when (state) {
                                AppUiState.Error -> ctx.toastUtil(
                                    "Unable to register " +
                                            "please try again later"
                                )

                                AppUiState.Loading -> ctx.toastUtil("Waiting for response")
                                is AppUiState.Success -> {

                                    ctx.toastUtil("Register Success")
                                    delay(2_000L)
                                    navigateToHome()
                                }
                            }
                        }
                    }

                },
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                )
            }
        }
    }
}