package com.mathandcoffee.cscodetest.rest.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteProductResponse(
    @Json(name = MESSAGE_KEY)
    val message: String,
    @Json(name = PRODUCT_ID_KEY)
    val productId: Int?
) {
    companion object {
        const val MESSAGE_KEY = "message"
        const val PRODUCT_ID_KEY = "product_id"
    }
}