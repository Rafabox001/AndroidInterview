package com.rdc.androidinterview.api.auth.network_models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Errors (

    @SerializedName("message")
    @Expose
    var message: String,

    @SerializedName("code")
    @Expose
    var code: String

) {
    override fun toString(): String {
        return "Errors(message='$message', code='$code')"
    }
}