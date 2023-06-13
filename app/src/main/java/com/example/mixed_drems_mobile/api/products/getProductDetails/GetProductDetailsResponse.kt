package com.example.mixed_drems_mobile.api.products.getProductDetails

import com.example.mixed_drems_mobile.api.products.Product
import com.example.mixed_drems_mobile.models.CartItem
import kotlinx.serialization.Serializable

@Serializable
data class GetProductDetailsResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val primaryImage: String?,
    val category: String,
    val company: ProductCompanyDto,
    val ingredients: List<IngredientDto>
)

fun GetProductDetailsResponse.toCartItem(count: Int = 1): CartItem {
    return CartItem(
        id = this.id,
        name = this.name,
        image = this.primaryImage,
        count = count,
        price = this.price
    )
}