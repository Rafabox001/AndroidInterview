package com.rdc.androidinterview.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.rdc.androidinterview.api.auth.ParrotChallengeApiAuthService
import com.rdc.androidinterview.api.auth.network_requests.LoginRequest
import com.rdc.androidinterview.api.auth.network_responses.LoginResponse
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.AuthToken
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.AuthTokenDao
import com.rdc.androidinterview.repository.JobManager
import com.rdc.androidinterview.repository.NetworkBoundResource
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.Response
import com.rdc.androidinterview.ui.ResponseType
import com.rdc.androidinterview.ui.auth.state.AuthViewState
import com.rdc.androidinterview.ui.auth.state.LoginFields
import com.rdc.androidinterview.util.AbsentLiveData
import com.rdc.androidinterview.util.ApiSuccessResponse
import com.rdc.androidinterview.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.rdc.androidinterview.util.GenericApiResponse
import com.rdc.androidinterview.util.PreferenceKeys
import com.rdc.androidinterview.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val parrotChallengeApiAuthService: ParrotChallengeApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
): JobManager("AuthRepository") {

    private val TAG: String = "AppDebug"

    fun attemptLogin(username: String, password: String): LiveData<DataState<AuthViewState>>{

        val loginFieldErrors = LoginFields(username, password).isValidForLogin()
        if(loginFieldErrors != LoginFields.LoginError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<LoginResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                // Don't care about result here. Just insert if it doesn't exist b/c of foreign key relationship
                // with AuthToken
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        pk = 1,
                        username = username,
                        password = password
                    )
                )

                // will return -1 if failure
                val result = authTokenDao.insert(
                    AuthToken(
                        account_pk = 1,
                        access_token = response.body.access_token,
                        refresh_token = response.body.refresh_token
                    )
                )
                if(result < 0){
                    return onCompleteJob(DataState.error(
                        Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog()))
                    )
                }

                saveAuthenticatedUserToPrefs(username)

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
                addJob("attemptLogin", job)
            }

            //Not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }

            //Not used in this case
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            //Not used in this case
            override suspend fun updateLocalDb(cacheObject: Any?) {
            }

        }.asLiveData()
    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>>{

        val previousAuthUserName: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if(previousAuthUserName.isNullOrBlank()){
            Log.d(TAG, "checkPreviousAuthUser: No previously authenticated user found.")
            return returnNoTokenFound()
        }
        else{
            return object: NetworkBoundResource<Void, Any, AuthViewState>(
                sessionManager.isConnectedToTheInternet(),
                false,
                false,
                false
            ){

                override suspend fun createCacheRequestAndReturn() {
                    accountPropertiesDao.searchByUserName(previousAuthUserName).let { accountProperties ->
                        Log.d(TAG, "createCacheRequestAndReturn: searching for token... account properties: ${accountProperties}")

                        accountProperties?.let {
                            if(accountProperties.pk > -1){
                                authTokenDao.searchByPk(accountProperties.pk).let { authToken ->
                                    if(authToken != null){
                                        if(authToken.access_token != null){
                                            onCompleteJob(
                                                DataState.data(
                                                    AuthViewState(authToken = authToken)
                                                )
                                            )
                                            return
                                        }
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "createCacheRequestAndReturn: AuthToken not found...")
                        onCompleteJob(
                            DataState.data(
                                null,
                                Response(
                                    RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                    ResponseType.None()
                                )
                            )
                        )
                    }
                }

                // not used in this case
                override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {
                }

                // not used in this case
                override fun createCall(): LiveData<GenericApiResponse<Void>> {
                    return AbsentLiveData.create()
                }

                //Not used in this case
                override fun loadFromCache(): LiveData<AuthViewState> {
                    return AbsentLiveData.create()
                }

                //Not used in this case
                override suspend fun updateLocalDb(cacheObject: Any?) {
                }

                override fun setJob(job: Job) {
                    addJob("checkPreviousAuthUser", job)
                }


            }.asLiveData()
        }
    }

    private fun saveAuthenticatedUserToPrefs(username: String){
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, username)
        sharedPrefsEditor.apply()
    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>>{
        return object: LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.data(null, Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None()))
            }
        }
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

}