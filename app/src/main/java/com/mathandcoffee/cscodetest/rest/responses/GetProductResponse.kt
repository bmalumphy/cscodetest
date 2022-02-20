package com.mathandcoffee.cscodetest.rest.responses

import com.mathandcoffee.cscodetest.rest.data.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetProductResponse(
    @Json(name = PRODUCT_KEY)
    val product: Product
) {
    companion object {
        const val PRODUCT_KEY = "product"
    }
}