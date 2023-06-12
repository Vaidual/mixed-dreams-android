package com.example.mixed_drems_mobile.enums

import com.example.mixed_drems_mobile.api.products.getProductDetails.MeasureUnitSerializer
import kotlinx.serialization.Serializable

@Serializable(with = MeasureUnitSerializer::class)
enum class MeasureUnit(val value: Int, val shortName: String? = null) {
    Item(1, "pieces"),
    Gram(10, "g"),
    Kilogram(11, "kg"),
    Ounce(12, "oz"),
    Cup(20, "cup"),
    Pint(30, "pt"),
    Liter(31, "l"),
    Milliliter(32, "ml");
}

fun MeasureUnit.getShort(): String {
    return shortName ?: name
}

fun MeasureUnit.getShortWithNumber(number: Number): String {
    return if (shortName != null) "$number$shortName" else "$number$name"
}