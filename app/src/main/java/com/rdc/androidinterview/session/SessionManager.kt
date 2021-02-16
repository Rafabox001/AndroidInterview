package com.rdc.androidinterview.session

import android.app.Application
import com.rdc.androidinterview.persistence.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {
}