package com.rdc.androidinterview.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.AuthToken
import com.rdc.androidinterview.models.MenuItem

@Database(entities = [AuthToken::class, AccountProperties::class, MenuItem::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class ParrotChallengeDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getMenuItemDao(): MenuItemDao

    companion object{
        val DATABASE_NAME: String = "parrot_challenge_db"
    }
}