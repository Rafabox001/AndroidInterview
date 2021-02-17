package com.rdc.androidinterview.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.StoreInfo

@Dao
interface AccountPropertiesDao {

    @Query("SELECT * FROM account_properties WHERE username = :username")
    suspend fun searchByUserName(username: String): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    fun searchByPk(pk: Int): LiveData<AccountProperties>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("UPDATE account_properties SET email = :email, username = :username, uuid = :uuid, stores = :stores WHERE pk = :pk")
    fun updateAccountProperties(pk: Int, email: String, username: String, uuid: String, stores: List<StoreInfo>)

}