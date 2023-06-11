package com.example.mixed_drems_mobile.pages.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mixed_drems_mobile.api.products.IProductsService
import com.example.mixed_drems_mobile.api.products.Product
import com.example.mixed_drems_mobile.api.products.getProducts.ProductFilterParams
import com.example.mixed_drems_mobile.paging.ProductsPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ProductsViewModel(
    productsService: IProductsService,
    defaultFilterParams: ProductFilterParams,

) : ViewModel() {

    private val pageSize = 10

    private val _pagingParams = MutableStateFlow(
        defaultFilterParams
    )

    private val pagingParams = _pagingParams.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = defaultFilterParams,
        )

    val productsFlow: Flow<PagingData<Product>> = pagingParams
        .debounce(500)
        .flatMapLatest {
            Pager(
                PagingConfig(
                    pageSize = pageSize,
                    enablePlaceholders = false,
                )
            ) {
                ProductsPagingSource(productsService, pagingParams.value, pageSize)
            }.flow.cachedIn(viewModelScope)
        }

    fun updateFilter(filter: ProductFilterParams) {
        if (filter == this._pagingParams.value) return
        this._pagingParams.value = filter
    }

    fun getFilter(): ProductFilterParams {
        return this._pagingParams.value
    }
}
