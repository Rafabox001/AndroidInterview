package com.rdc.androidinterview.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("uuid")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uuid") var uuid: String,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name") var name: String,

    @SerializedName("sortPosition")
    @Expose
    @ColumnInfo(name = "sortPosition") var sortPosition: Int
) {

    override fun toString(): String {
        return "Category(uuid=$uuid, " +
                "name='$name', " +
                "sortPosition='$sortPosition')"
    }
}