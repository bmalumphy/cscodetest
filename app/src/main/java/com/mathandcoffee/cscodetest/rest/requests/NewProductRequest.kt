package com.mathandcoffee.cscodetest.rest.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class NewProductRequest(
    @Json(name = BAD_KEY)
    val badKey: String,
    @Json(name = NAME_KEY)
    val name: String,
    @Json(name = DESCRIPTION_KEY)
    val description: String,
    @Json(name = STYLE_KEY)
    val style: String,
    @Json(name = BRAND_KEY)
    val brand: String,
    @Json(name = SHIPPING_PRICE_CENTS_KEY)
    val shippingPriceCents: Int
) {
    companion object {
        const val NAME_KEY = "name"
        const val BAD_KEY = "asdf"
        const val DESCRIPTION_KEY = "description"
        const val STYLE_KEY = "style"
        const val BRAND_KEY = "brand"
        const val SHIPPING_PRICE_CENTS_KEY = "shipping_price_cents"
    }
}
