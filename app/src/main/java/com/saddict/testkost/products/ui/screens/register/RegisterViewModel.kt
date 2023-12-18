package com.saddict.testkost.products.ui.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddict.testkost.products.data.manager.AppUiState
import com.saddict.testkost.products.data.manager.PreferenceDataStore
import com.saddict.testkost.products.data.remote.remository.ApiRepository
import com.saddict.testkost.products.model.remote.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val userPreferenceFlow: PreferenceDataStore
) : ViewModel() {
    private val _uiState = MutableSharedFlow<AppUiState>()
    val uiState: SharedFlow<AppUiState> = _uiState

    fun register(
        username: String,
        password: String,
        email: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _uiState.emit(AppUiState.Loading)
                    val user = RegisterUser(
                        password = password, username = username, email = email
                    )
                    val register = repository.register(user)
                    if (register.isSuccessful) {
                        val responseBody = register.body()
                        val token = responseBody!!.token
                        userPreferenceFlow.setToken(token)
                        Log.d("Success", responseBody.toString())
                        _uiState.emit(AppUiState.Success)
                    } else {
                        _uiState.emit(AppUiState.Error)
                        val errorBody = register.raw()
                        Log.e("NotSent", "Error: $errorBody")
                    }
                } catch (e: IOException) {
                    Log.e("RegisterError", "Logging in error $e")
                }
            }
        }
    }
}