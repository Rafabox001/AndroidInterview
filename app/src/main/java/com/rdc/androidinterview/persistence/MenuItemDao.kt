package com.rdc.androidinterview.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rdc.androidinterview.models.MenuItem

@Dao
interface MenuItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(menuItem: MenuItem): Long

    @Query("SELECT * FROM menu_item_info")
    fun getAllMenuItems(): LiveData<List<MenuItem>>

}