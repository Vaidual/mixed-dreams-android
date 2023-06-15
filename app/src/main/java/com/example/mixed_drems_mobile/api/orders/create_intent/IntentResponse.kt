package com.example.mixed_drems_mobile.api.orders.create_intent

import kotlinx.serialization.Serializable

@Serializable
data class IntentResponse(
    val clientSecret: String,
    val ephemeralKey: String,
    val publishableKey: String,
    val customerId: String,
)
