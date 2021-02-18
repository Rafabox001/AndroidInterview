package com.rdc.androidinterview.api.auth

import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.GenericStatusResponse
import com.rdc.androidinterview.api.auth.network_requests.LoginRequest
import com.rdc.androidinterview.api.auth.network_requests.RefreshRequest
import com.rdc.androidinterview.api.auth.network_responses.LoginResponse
import com.rdc.androidinterview.util.GenericApiResponse
import retrofit2.http.*

interface ParrotChallengeApiAuthService {

    @Headers("Content-Type: application/json")
    @POST("auth/token")
    fun login(
        @Body loginRequest: LoginRequest
    ): LiveData<GenericApiResponse<LoginResponse>>

    @Headers("Content-Type: application/json")
    @POST("auth/token/refresh")
    fun refreshToken(
        @Body refreshRequest: RefreshRequest
    ): LiveData<GenericApiResponse<LoginResponse>>

    @Headers("Content-Type: application/json")
    @GET("auth/token/test")
    fun validateToken(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<GenericStatusResponse>>

}