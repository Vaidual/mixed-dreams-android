package com.example.mixed_drems_mobile.presentation.pages.shoppingCart

import com.example.mixed_drems_mobile.api.orders.create_intent.IntentResponse
import com.example.mixed_drems_mobile.api.products.getProductDetails.GetProductDetailsResponse
import com.example.mixed_drems_mobile.models.ErrorResponse
import com.example.mixed_drems_mobile.presentation.common.BaseState

data class StripeState(
    override val isLoading: Boolean = false,
    val intent: IntentResponse? = null,
    val successOrder: Boolean = false,
    override val error: ErrorResponse? = null
) : BaseState<IntentResponse>(isLoading, intent, error)