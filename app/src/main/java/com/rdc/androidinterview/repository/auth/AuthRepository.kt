package com.rdc.androidinterview.repository.auth

import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.auth.ParrotChallengeApiAuthService
import com.rdc.androidinterview.api.auth.network_requests.LoginRequest
import com.rdc.androidinterview.api.auth.network_responses.LoginResponse
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.AuthTokenDao
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val parrotChallengeApiAuthService: ParrotChallengeApiAuthService,
    val sessionManager: SessionManager
) {

    fun testLoginRequest(loginRequest: LoginRequest): LiveData<GenericApiResponse<LoginResponse>> {
        return parrotChallengeApiAuthService.login(loginRequest)
    }

}