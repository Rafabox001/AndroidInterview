package com.rdc.androidinterview.api.auth.network_requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginRequest (
    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("password")
    @Expose
    var password: String
)