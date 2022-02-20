package com.mathandcoffee.cscodetest.rest.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.net.URL

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = ID_KEY)
    val id: Int,
    @Json(name = NAME_KEY)
    val productName: String,
    @Json(name = DESCRIPTION_KEY)
    val description: String,
    @Json(name = STYLE_KEY)
    val style: String,
    @Json(name = BRAND_KEY)
    val brand: String,
    @Json(name = URL_KEY)
    val url: String?,
    @Json(name = SHIPPING_PRICE_KEY)
    val shippingPrice: String,
    @Json(name = PRODUCT_TYPE_KEY)
    val productType: String?
) {
    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "product_name"
        const val DESCRIPTION_KEY = "description"
        const val STYLE_KEY = "style"
        const val BRAND_KEY = "brand"
        const val URL_KEY = "url"
        const val SHIPPING_PRICE_KEY = "shipping_price"
        const val PRODUCT_TYPE_KEY = "product_type"
    }
}