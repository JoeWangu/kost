package com.saddict.testkost.products.data.manager

interface PreferenceDataStore {
    fun getToken(): String
    suspend fun setToken(token: String?)
}