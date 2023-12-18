package com.saddict.testkost.products.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.saddict.testkost.products.model.local.ProductEntity
import com.saddict.testkost.utils.DataMapper.Companion.mapToResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    pager: Pager<Int, ProductEntity>
): ViewModel() {
    val productPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.mapToResults() }
        }
        .cachedIn(viewModelScope)
}