package com.saddict.testkost.products.data.manager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.saddict.testkost.utils.tokenDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okio.IOException
import javax.inject.Inject

class PreferenceDataStoreImpl @Inject constructor(
    @ApplicationContext context: Context
): PreferenceDataStore {
    private val preference = context.tokenDataStore
    private object PreferenceKeys{
        val TOKEN_KEY = stringPreferencesKey("token_key")
    }
    private val preferenceFlow: Flow<String> = preference.data
        .catch {
            if (it is IOException){
                it.printStackTrace()
                emit(emptyPreferences())
            }else{
                throw it
            }
        }
        .map { preferences->
            preferences[PreferenceKeys.TOKEN_KEY] ?: ""
        }
    override fun getToken(): String{
        var token: String
        runBlocking {
            token = preferenceFlow.first()
        }
        return token
    }
    override suspend fun setToken(token: String?){
        preference.edit { preferences->
            preferences[PreferenceKeys.TOKEN_KEY] = token ?: ""
        }
    }
}