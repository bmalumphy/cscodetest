package com.mathandcoffee.cscodetest.rest.responses

import com.mathandcoffee.cscodetest.rest.data.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GetProductsResponse(
    @Json(name = COUNT_KEY)
    val count: Int,
    @Json(name = PRODUCTS_KEY)
    val products: List<Product>
) {
    companion object {
        const val COUNT_KEY = "count"
        const val PRODUCTS_KEY = "products"
    }
}
