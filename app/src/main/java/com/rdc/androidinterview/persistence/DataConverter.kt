package com.rdc.androidinterview.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rdc.androidinterview.models.Category
import com.rdc.androidinterview.models.StoreInfo
import java.lang.reflect.Type


class DataConverter {
    @TypeConverter
    fun fromStoreInfoList(countryLang: List<StoreInfo?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<StoreInfo?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toStoreInfoList(countryLangString: String?): List<StoreInfo>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<StoreInfo?>?>() {}.type
        return gson.fromJson<List<StoreInfo>>(countryLangString, type)
    }

    @TypeConverter
    fun fromCategory(category: Category?): String? {
        if (category == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Category?>() {}.type
        return gson.toJson(category, type)
    }

    @TypeConverter
    fun toCategory(categoryString: String?): Category? {
        if (categoryString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Category?>() {}.type
        return gson.fromJson<Category>(categoryString, type)
    }
}