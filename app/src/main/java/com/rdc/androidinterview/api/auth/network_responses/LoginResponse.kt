package com.rdc.androidinterview.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rdc.androidinterview.api.auth.network_models.Errors

class LoginResponse(

    @SerializedName("refresh")
    @Expose
    var refresh_token: String,

    @SerializedName("access")
    @Expose
    var access_token: String,

    @SerializedName("errors")
    @Expose
    var errors: Errors,

    @SerializedName("validation")
    @Expose
    var validation: String?

)
{
    override fun toString(): String {
        return "LoginResponse(refresh_token='$refresh_token', access_token='$access_token', errors='${errors}', validation='$validation')"
    }
}
