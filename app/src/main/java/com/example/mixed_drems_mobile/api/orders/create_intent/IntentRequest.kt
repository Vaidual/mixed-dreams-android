package com.example.mixed_drems_mobile.api.orders.create_intent

import kotlinx.serialization.Serializable

@Serializable
data class IntentRequest(
    val amount: Long
)
