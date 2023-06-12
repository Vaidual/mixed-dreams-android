package com.example.mixed_drems_mobile.api.products.getProductDetails

import com.example.mixed_drems_mobile.enums.MeasureUnit
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class IngredientDto(
    val name: String,
    val hasAmount: Boolean,
    val amount: Float?,
    val unit: MeasureUnit?,
)

object MeasureUnitSerializer: KSerializer<MeasureUnit> {
    override fun deserialize(decoder: Decoder): MeasureUnit {
        val value = decoder.decodeInt()
        return MeasureUnit.values().find { it.value == value }
            ?: throw IllegalArgumentException("Invalid ${MeasureUnit::class.simpleName ?: "MeasureUnit"} value: $value")
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(MeasureUnit::class.simpleName ?: "MeasureUnit", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: MeasureUnit) {
        encoder.encodeInt(value.value)
    }
}