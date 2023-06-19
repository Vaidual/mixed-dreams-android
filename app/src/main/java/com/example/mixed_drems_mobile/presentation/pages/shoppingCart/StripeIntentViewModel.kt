package com.example.mixed_drems_mobile.presentation.pages.shoppingCart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixed_drems_mobile.api.orders.IOrdersRepository
import com.example.mixed_drems_mobile.api.orders.create_intent.IntentRequest
import com.example.mixed_drems_mobile.api.orders.create_intent.IntentResponse
import com.example.mixed_drems_mobile.api.orders.post_order.PostOrderProductDto
import com.example.mixed_drems_mobile.api.orders.post_order.PostOrderRequest
import com.example.mixed_drems_mobile.presentation.common.Resource
import com.example.mixed_drems_mobile.utils.MainApplication
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StripeIntentViewModel @Inject constructor(
    private val orderRepo: IOrdersRepository
) : ViewModel() {
    private val _state = MutableStateFlow(StripeState())
    val state = _state.asStateFlow()

    fun createPaymentIntent(amount: Long) {

        val productFlow: Flow<Resource<IntentResponse>> = flow {
            emit(Resource.Loading())
            val response = orderRepo.createIntent(IntentRequest(amount))
            if (response.isSuccess) {
                emit(Resource.Success(response.data!!))
            } else {
                emit(Resource.Error(response.error!!))
            }
        }
        productFlow.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = StripeState(intent = result.data)
                }

                is Resource.Error -> {
                    _state.value = StripeState(error = result.error)
                }

                is Resource.Loading -> {
                    _state.value = StripeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun handlePaymentResult(result: PaymentSheetResult) {
        when (result) {
            PaymentSheetResult.Canceled -> {
                println("Canceled")
            }

            PaymentSheetResult.Completed -> {
                creteOrder(
                    PostOrderRequest(
                        businessLocationId = "",
                        products = MainApplication.instance.shoppingCart.cartItems.map {
                            PostOrderProductDto(productId = it.id, amount = it.count)
                        })
                )
                println("Completed")
            }

            is PaymentSheetResult.Failed -> {
                println("Failed")
            }
        }
    }

    fun creteOrder(order: PostOrderRequest) {
//        val orderFlow: Flow<Resource<Unit>> =flow {
//            emit(Resource.Loading())
//            val response = orderRepo.createOrder(order)
//        }
        MainApplication.instance.shoppingCart.clear()
        _state.value = _state.value.copy(successOrder = true)
    }

    fun onPaymentLaunched() {
        _state.value = StripeState()
    }

    fun onSuccessEnd() {
        _state.value = StripeState()
    }
}