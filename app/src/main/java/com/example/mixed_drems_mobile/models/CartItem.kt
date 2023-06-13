package com.example.mixed_drems_mobile.models

data class CartItem(
    val id: String,
    val name: String,
    val image: String?,
    val price: Double,
    var count: Int,
)