package com.rdc.androidinterview.api.auth.network_requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class RefreshRequest (
    @SerializedName("refresh")
    @Expose
    var refresh: String,
)