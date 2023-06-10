package com.example.mixed_drems_mobile.api.products.getProducts

import com.example.mixed_drems_mobile.api.products.Product
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GetProductsResponse(
    val products: List<Product>,
    val totalCount: Int
)