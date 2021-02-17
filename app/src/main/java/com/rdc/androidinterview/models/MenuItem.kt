package com.rdc.androidinterview.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "menu_item_info")
data class MenuItem(
    @SerializedName("uuid")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uuid") var uuid: String,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name") var name: String,

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description") var description: String,

    @SerializedName("imageUrl")
    @Expose
    @ColumnInfo(name = "imageUrl") var imageUrl: String,

    @SerializedName("legacyId")
    @Expose
    @ColumnInfo(name = "legacyId") var legacyId: String,

    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price") var price: String,

    @SerializedName("alcoholCount")
    @Expose
    @ColumnInfo(name = "alcoholCount") var alcoholCount: Int,

    @SerializedName("soldAlone")
    @Expose
    @ColumnInfo(name = "soldAlone") var soldAlone: Boolean,

    @SerializedName("availability")
    @Expose
    @ColumnInfo(name = "availability") var availability: String,

    @SerializedName("category")
    @Expose
    @ColumnInfo(name = "category") var category: Category
) {

    override fun toString(): String {
        return "MenuItem(uuid=$uuid, " +
                "name='$name', " +
                "description='$description', " +
                "imageUrl='$imageUrl', " +
                "legacyId=$legacyId, " +
                "price=$price, " +
                "alcoholCount=$alcoholCount, " +
                "soldAlone=$soldAlone, " +
                "availability=$availability, " +
                "category='$category')"
    }

}