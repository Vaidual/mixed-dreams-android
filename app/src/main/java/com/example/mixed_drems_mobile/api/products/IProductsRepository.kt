package com.example.mixed_drems_mobile.api.products

import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.products.getProductDetails.GetProductDetailsResponse
import com.example.mixed_drems_mobile.api.products.getProducts.GetProductsResponse

interface IProductsRepository {
    suspend fun getProducts(
        page: Int?,
        size: Int?,
        key: String?,
        category: String?,
        sort: String?,
    ): ApiResponse<GetProductsResponse>

    suspend fun getProductDetails(
        id: String,
    ): ApiResponse<GetProductDetailsResponse>
}