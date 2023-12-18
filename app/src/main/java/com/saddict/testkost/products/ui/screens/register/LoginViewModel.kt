package com.saddict.testkost.products.ui.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddict.testkost.products.data.manager.PreferenceDataStore
import com.saddict.testkost.products.data.remote.remository.ApiRepository
import com.saddict.testkost.products.model.remote.User
import com.saddict.testkost.products.model.remote.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

sealed interface LoginUiState {
    data class Success(val userResponse: UserResponse) : LoginUiState
    data object Error : LoginUiState
    data object Loading : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val userPreferenceFlow: PreferenceDataStore
): ViewModel() {
    private val _uiState = MutableSharedFlow<LoginUiState>()
    val uiState: SharedFlow<LoginUiState> = _uiState
    fun login(
        username: String,
        password: String,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _uiState.emit(LoginUiState.Loading)
                    val user = User(password = password, username = username)
                    val login = repository.login(user)
                    if (login.isSuccessful) {
                        val responseBody = login.body()
                        val token = responseBody!!.token
                        userPreferenceFlow.setToken(token)
                        Log.d("Success", responseBody.toString())
                        _uiState.emit(LoginUiState.Success(responseBody))
                    } else {
                        _uiState.emit(LoginUiState.Error)
                        val errorBody = login.raw()
                        Log.e("NotSent", "Error: $errorBody")
                    }
                } catch (e: IOException) {
                    Log.e("LoginError", "Logging in error $e")
                }
            }
        }
    }
}