package com.rdc.androidinterview.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "store_info")
data class StoreInfo(
    @SerializedName("uuid")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uuid") var uuid: String,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name") var name: String,

    @SerializedName("availabilityState")
    @Expose
    @ColumnInfo(name = "availabilityState") var availabilityState: String
) {


}