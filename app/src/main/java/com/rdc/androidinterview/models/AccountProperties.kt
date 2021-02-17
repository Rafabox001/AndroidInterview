package com.rdc.androidinterview.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rdc.androidinterview.persistence.DataConverter

@Entity(tableName = "account_properties")
data class AccountProperties(

    @SerializedName("pk")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk") var pk: Int,

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "username") var username: String,

    @SerializedName("password")
    @Expose
    @ColumnInfo(name = "password") var password: String,

    @SerializedName("uuid")
    @Expose
    @ColumnInfo(name = "uuid") var uuid: String? = null,

    @SerializedName("email")
    @Expose
    @ColumnInfo(name = "email") var email: String? = null,

    @SerializedName("stores")
    @Expose
    @ColumnInfo(name = "stores") var stores: List<StoreInfo>? = listOf()
)
{

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as AccountProperties

        if (pk != other.pk) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (uuid != other.uuid) return false
        if (email != other.email) return false
        if (stores != other.stores) return false

        return true
    }

}