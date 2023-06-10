package com.example.mixed_drems_mobile.api.products

import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.products.getProducts.GetProductsResponse

interface IProductsService {

//    @GET("/products/{id}")
//    suspend fun getProduct(@Path("id") id:String): Response<Product>

    suspend fun getProducts(
        page: Int?,
        size: Int?,
        key: String?,
        category: String?,
        sort: String?,
    ): ApiResponse<GetProductsResponse>
}