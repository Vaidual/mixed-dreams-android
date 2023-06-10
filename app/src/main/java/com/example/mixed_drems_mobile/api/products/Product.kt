package com.example.mixed_drems_mobile.api.products

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String?,

    @field:SerializedName("price")
    val price: Float?,

    @field:SerializedName("primaryImage")
    val primaryImage: String?,
)
