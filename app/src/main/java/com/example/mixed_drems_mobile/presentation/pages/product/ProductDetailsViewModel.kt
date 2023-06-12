package com.example.mixed_drems_mobile.presentation.pages.product

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mixed_drems_mobile.api.products.IProductsRepository
import com.example.mixed_drems_mobile.constants.RoutingConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.mixed_drems_mobile.api.products.getProductDetails.GetProductDetailsResponse
import com.example.mixed_drems_mobile.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productsService: IProductsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = mutableStateOf(ProductDetailsState())
    val state: State<ProductDetailsState> = _state

    init {
        savedStateHandle.get<String>(RoutingConstants.PRODUCT_ID)?.let { coinId ->
            getProduct(coinId)
        }
    }

    private fun getProduct(productId: String) {
        val productFlow: Flow<Resource<GetProductDetailsResponse>> = flow {
            emit(Resource.Loading())
            val response = productsService.getProductDetails(productId)
            if (response.isSuccess) {
                emit(Resource.Success(response.data!!))
            } else {
                emit(Resource.Error(response.error!!))
            }
        }
        productFlow.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ProductDetailsState(product = result.data)
                }
                is Resource.Error -> {
                    _state.value = ProductDetailsState(error = result.error)
                }
                is Resource.Loading -> {
                    _state.value = ProductDetailsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}