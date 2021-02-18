package com.rdc.androidinterview.api.menu.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.MenuItem

class MenuUpdateResponse(

    @SerializedName("status")
    @Expose
    var status: String,

    @SerializedName("result")
    @Expose
    var result: MenuItem,

) {
    override fun toString(): String {
        return "LoginResponse(STATUS='$status', result='$result')"
    }
}
