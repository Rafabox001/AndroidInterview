package com.rdc.androidinterview.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.AuthToken

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class ParrotChallengeDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        val DATABASE_NAME: String = "parrot_challenge_db"
    }
}