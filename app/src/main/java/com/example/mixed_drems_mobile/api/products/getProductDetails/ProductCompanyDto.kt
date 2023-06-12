package com.example.mixed_drems_mobile.api.products.getProductDetails

import kotlinx.serialization.Serializable

@Serializable
data class ProductCompanyDto(
    val id: String,
    val name: String
)
