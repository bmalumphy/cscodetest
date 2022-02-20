package com.mathandcoffee.cscodetest.rest

import com.mathandcoffee.cscodetest.rest.responses.GetProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductAPIService {

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<GetProductsResponse>
}