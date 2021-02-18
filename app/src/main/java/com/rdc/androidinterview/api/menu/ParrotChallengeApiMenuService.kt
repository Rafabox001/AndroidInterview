package com.rdc.androidinterview.api.menu

import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.menu.network_requests.MenuUpdateRequest
import com.rdc.androidinterview.api.menu.network_responses.MenuListResponse
import com.rdc.androidinterview.api.menu.network_responses.MenuUpdateResponse
import com.rdc.androidinterview.api.menu.network_responses.MyStoreResponse
import com.rdc.androidinterview.util.GenericApiResponse
import retrofit2.http.*

interface ParrotChallengeApiMenuService {

    @GET("v1/users/me")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<MyStoreResponse>>

    @GET("v1/products/")
    fun searchListMenuItems(
        @Header("Authorization") authorization: String,
        @Query("store") storeId: String
    ): LiveData<GenericApiResponse<MenuListResponse>>

    @Headers("Content-Type: application/json")
    @PUT("v1/products/{product_id}/availability")
    fun updateMenuItem(
        @Header("Authorization") authorization: String,
        @Path("product_id") product_id: String,
        @Body menuUpdateRequest: MenuUpdateRequest
    ): LiveData<GenericApiResponse<MenuUpdateResponse>>
}