package com.mathandcoffee.cscodetest.rest.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    @Json(name = TOKEN_KEY)
    val token: String?,
    @Json(name = ERROR_KEY)
    val error: Int
) {
    companion object {
        const val TOKEN_KEY = "token"
        const val ERROR_KEY = "error"
    }
}