package com.mathandcoffee.cscodetest.rest

import com.mathandcoffee.cscodetest.rest.data.Product
import com.mathandcoffee.cscodetest.rest.requests.NewProductRequest
import com.mathandcoffee.cscodetest.rest.responses.DeleteProductResponse
import com.mathandcoffee.cscodetest.rest.responses.GetProductResponse
import com.mathandcoffee.cscodetest.rest.responses.GetProductsResponse
import com.mathandcoffee.cscodetest.rest.responses.NewProductResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductAPIService {

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<GetProductsResponse>

    @POST("product")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body request: NewProductRequest
    ): Response<NewProductResponse>

    @DELETE("product/{productId}")
    suspend fun deleteProduct(
        @Path(value = "productId") productId: Int,
        @Header("Authorization") token: String
    ): Response<DeleteProductResponse>

    @GET("product/{productId}")
    suspend fun getProduct(
        @Path(value = "productId") productId: Int,
        @Header("Authorization") token: String
    ): Response<GetProductResponse>
}