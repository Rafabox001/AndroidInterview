package com.rdc.androidinterview.repository.auth

import com.rdc.androidinterview.api.auth.ParrotChallengeApiAuthService
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.AuthTokenDao
import com.rdc.androidinterview.session.SessionManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val parrotChallengeApiAuthService: ParrotChallengeApiAuthService,
    val sessionManager: SessionManager
) {
}