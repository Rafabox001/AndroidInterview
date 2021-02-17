package com.rdc.androidinterview.api.menu

import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.menu.network_responses.MenuListResponse
import com.rdc.androidinterview.api.menu.network_responses.MyStoreResponse
import com.rdc.androidinterview.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

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
}