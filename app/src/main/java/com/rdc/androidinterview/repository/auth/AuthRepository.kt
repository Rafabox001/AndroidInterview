package com.rdc.androidinterview.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.auth.ParrotChallengeApiAuthService
import com.rdc.androidinterview.api.auth.network_requests.LoginRequest
import com.rdc.androidinterview.api.auth.network_responses.LoginResponse
import com.rdc.androidinterview.models.AuthToken
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.AuthTokenDao
import com.rdc.androidinterview.repository.NetworkBoundResource
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.Response
import com.rdc.androidinterview.ui.ResponseType
import com.rdc.androidinterview.ui.auth.state.AuthViewState
import com.rdc.androidinterview.ui.auth.state.LoginFields
import com.rdc.androidinterview.util.ApiSuccessResponse
import com.rdc.androidinterview.util.GenericApiResponse
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val parrotChallengeApiAuthService: ParrotChallengeApiAuthService,
    val sessionManager: SessionManager
) {

    private val TAG: String = "AppDebug"

    private var repositoryJob: Job? = null

    fun attemptLogin(username: String, password: String): LiveData<DataState<AuthViewState>>{

        val loginFieldErrors = LoginFields(username, password).isValidForLogin()
        if(loginFieldErrors != LoginFields.LoginError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(
                                account_pk = 1,
                                access_token = response.body.access_token,
                                refresh_token = response.body.refresh_token
                            )
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return parrotChallengeApiAuthService.login(
                    LoginRequest(
                        username,
                        password
                    )
                )
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<AuthViewState>>{
        Log.d(TAG, "returnErrorResponse: ${errorMessage}")

        return object: LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun cancelActiveJobs(){
        Log.d(TAG, "AuthRepository: Cancelling on-going jobs...")
        repositoryJob?.cancel()
    }

}