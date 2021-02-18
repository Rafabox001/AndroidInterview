package com.rdc.androidinterview.api.menu.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.MenuItem

class MenuListResponse(

    @SerializedName("status")
    @Expose
    var status: String,

    @SerializedName("results")
    @Expose
    var results: List<MenuItem>,

) {
    override fun toString(): String {
        return "LoginResponse(STATUS='$status', results='$results')"
    }
}
