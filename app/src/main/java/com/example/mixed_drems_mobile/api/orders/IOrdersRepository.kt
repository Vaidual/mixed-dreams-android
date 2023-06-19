package com.example.mixed_drems_mobile.api.orders

import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.orders.create_intent.IntentRequest
import com.example.mixed_drems_mobile.api.orders.create_intent.IntentResponse
import com.example.mixed_drems_mobile.api.orders.post_order.PostOrderRequest

interface IOrdersRepository {
    suspend fun createIntent(
        intentRequest: IntentRequest,
    ): ApiResponse<IntentResponse>

    suspend fun createOrder(
        intentRequest: PostOrderRequest,
    ): ApiResponse<Unit>
}