package com.rdc.androidinterview.ui.auth

import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.auth.network_requests.LoginRequest
import com.rdc.androidinterview.models.AuthToken
import com.rdc.androidinterview.repository.auth.AuthRepository
import com.rdc.androidinterview.ui.BaseViewModel
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.auth.state.AuthStateEvent
import com.rdc.androidinterview.ui.auth.state.AuthStateEvent.*
import com.rdc.androidinterview.ui.auth.state.AuthViewState
import com.rdc.androidinterview.ui.auth.state.LoginFields
import com.rdc.androidinterview.util.AbsentLiveData
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    val authRepository: AuthRepository
): BaseViewModel<AuthStateEvent, AuthViewState>(){

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        return when(stateEvent){
            is LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.username,
                    stateEvent.password
                )
            }
            is CheckPreviousAuthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }
            is None -> {
                return object: LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null, null)
                    }
                }
            }
        }
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }

    private fun handlePendingData(){
        setStateEvent(None())
    }

    fun cancelActiveJobs(){
        handlePendingData()
        authRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}