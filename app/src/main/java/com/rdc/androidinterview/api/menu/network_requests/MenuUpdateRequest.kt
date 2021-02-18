package com.rdc.androidinterview.api.menu.network_requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MenuUpdateRequest (
    @SerializedName("availability")
    @Expose
    var availability: String
)