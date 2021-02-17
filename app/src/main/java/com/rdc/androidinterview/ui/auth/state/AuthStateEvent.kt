package com.rdc.androidinterview.ui.auth.state

sealed class AuthStateEvent{

    data class LoginAttemptEvent(
        val username: String,
        val password: String
    ): AuthStateEvent()

    class CheckPreviousAuthEvent(): AuthStateEvent()

    class None(): AuthStateEvent()
}