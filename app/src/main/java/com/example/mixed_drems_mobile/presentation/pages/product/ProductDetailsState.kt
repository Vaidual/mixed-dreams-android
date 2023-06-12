package com.example.mixed_drems_mobile.presentation.pages.product

import com.example.mixed_drems_mobile.api.products.getProductDetails.GetProductDetailsResponse
import com.example.mixed_drems_mobile.models.ErrorResponse
import com.example.mixed_drems_mobile.presentation.common.BaseState

data class ProductDetailsState(
    override val isLoading: Boolean = false,
    val product: GetProductDetailsResponse? = null,
    override val error: ErrorResponse? = null
) : BaseState<GetProductDetailsResponse>(isLoading, product, error)