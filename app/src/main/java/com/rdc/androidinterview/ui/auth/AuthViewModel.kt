package com.rdc.androidinterview.ui.auth

import androidx.lifecycle.ViewModel
import com.rdc.androidinterview.repository.auth.AuthRepository

class AuthViewModel constructor(
    val authRepository: AuthRepository
): ViewModel(){
    
}