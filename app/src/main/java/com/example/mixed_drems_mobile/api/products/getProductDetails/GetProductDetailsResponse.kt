package com.example.mixed_drems_mobile.api.products.getProductDetails

import kotlinx.serialization.Serializable

@Serializable
data class GetProductDetailsResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val primaryImage: String,
)