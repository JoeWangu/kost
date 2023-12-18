package com.saddict.testkost.utils

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore

val Context.tokenDataStore by preferencesDataStore(name = Constants.TOKEN)
fun Context.toastUtil(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.toastUtilLong(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
