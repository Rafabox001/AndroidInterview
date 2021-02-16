package com.rdc.androidinterview.ui.auth.state

sealed class AuthStateEvent{

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ): AuthStateEvent()

    class CheckPreviousAuthEvent(): AuthStateEvent()
}