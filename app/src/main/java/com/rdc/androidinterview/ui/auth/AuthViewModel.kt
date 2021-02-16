package com.rdc.androidinterview.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rdc.androidinterview.api.auth.network_requests.LoginRequest
import com.rdc.androidinterview.api.auth.network_responses.LoginResponse
import com.rdc.androidinterview.repository.auth.AuthRepository
import com.rdc.androidinterview.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    val authRepository: AuthRepository
): ViewModel(){

    fun testLogin(): LiveData<GenericApiResponse<LoginResponse>> {
        val loginRequest = LoginRequest(
            username = "android-challenge@parrotsoftware.io",
            password = "8mngDhoPcB3ckV7X"
        )

        return authRepository.testLoginRequest(
            loginRequest
        )
    }

}