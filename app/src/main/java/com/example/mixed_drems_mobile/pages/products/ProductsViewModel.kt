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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ProductsViewModel(
    productsService: IProductsService,
//    private val pagingParams: ProductPaginationParams,

) : ViewModel() {

    private val pageSize = 10

    private val pagingParams = MutableLiveData(
        ProductFilterParams(
            null,
            null,
            null
        )
    )

    private val pagingConfig = PagingConfig(
        pageSize = pageSize, // Number of items loaded per page
        enablePlaceholders = false // Whether to show placeholders for unloaded items
    )

    private val pagingSource = ProductsPagingSource(productsService, pagingParams.value!!, pageSize)

    private val pager = Pager(config = pagingConfig) {
        pagingSource
    }

    val productsFlow: Flow<PagingData<Product>> = pagingParams.asFlow()
        .debounce(500)
        .flatMapLatest {
            Pager(config = pagingConfig) {
                pagingSource
            }.flow
        }
        .cachedIn(viewModelScope)

    fun updateFilter(filter: ProductFilterParams) {
        if (filter == this.pagingParams.value) return
        this.pagingParams.value = filter
        println(this.pagingParams.value)
    }

    fun getFilter(): ProductFilterParams {
        return this.pagingParams.value!!
    }
}
