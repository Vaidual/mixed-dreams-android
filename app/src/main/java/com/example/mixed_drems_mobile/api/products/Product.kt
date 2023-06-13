package com.example.mixed_drems_mobile.api.products

import com.example.mixed_drems_mobile.models.CartItem
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
    val price: Double,

    @field:SerializedName("primaryImage")
    val primaryImage: String?,
)

fun Product.toCartItem(count: Int = 1): CartItem {
    return CartItem(
        id = this.id,
        name = this.name,
        image = this.primaryImage,
        count = count,
        price = this.price
    )
}