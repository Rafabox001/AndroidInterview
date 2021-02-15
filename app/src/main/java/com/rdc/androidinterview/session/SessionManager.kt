package com.rdc.androidinterview.session

import android.app.Application
import com.rdc.androidinterview.persistence.AuthTokenDao

class SessionManager constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {
}