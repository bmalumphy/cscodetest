package com.mathandcoffee.cscodetest.rest

import com.mathandcoffee.cscodetest.rest.responses.AuthTokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthenticationAPIService {

    @GET("status")
    suspend fun getUserToken(@Header("Authorization") loginToken: String): Response<AuthTokenResponse>
}