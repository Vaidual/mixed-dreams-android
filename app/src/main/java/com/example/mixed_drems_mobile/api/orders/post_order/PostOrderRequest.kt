package com.example.mixed_drems_mobile.api.orders.post_order

data class PostOrderRequest(
    val businessLocationId: String,
    val products: List<PostOrderProductDto>
)
