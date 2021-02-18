package com.rdc.androidinterview.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenericStatusResponse(
    @SerializedName("status")
    @Expose
    var status: String
)