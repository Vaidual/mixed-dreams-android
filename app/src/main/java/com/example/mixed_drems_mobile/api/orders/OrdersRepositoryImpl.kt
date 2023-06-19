package com.example.mixed_drems_mobile.api.orders

import com.example.mixed_drems_mobile.api.ApiClient
import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.ApiRoutes
import com.example.mixed_drems_mobile.api.orders.create_intent.IntentRequest
import com.example.mixed_drems_mobile.api.orders.create_intent.IntentResponse
import com.example.mixed_drems_mobile.api.orders.post_order.PostOrderRequest
import com.example.mixed_drems_mobile.api.postWithApiResponse
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class OrdersRepositoryImpl: IOrdersRepository {
    override suspend fun createIntent(intentRequest: IntentRequest): ApiResponse<IntentResponse> {
        val result = ApiClient.client.postWithApiResponse<IntentResponse> {
            url(ApiRoutes.CreateIntent)
            method = HttpMethod.Post
            setBody(intentRequest)
        }

        return result
    }

    override suspend fun createOrder(order: PostOrderRequest): ApiResponse<Unit> {
        val result = ApiClient.client.postWithApiResponse<Unit> {
            url(ApiRoutes.CreateIntent)
            method = HttpMethod.Post
            setBody(order)
        }

        return result
    }

}