package com.rdc.androidinterview.repository.menu.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.rdc.androidinterview.api.menu.ParrotChallengeApiMenuService
import com.rdc.androidinterview.api.menu.network_responses.MyStoreResponse
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.AuthToken
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.repository.JobManager
import com.rdc.androidinterview.repository.NetworkBoundResource
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.menu.account.state.AccountViewState
import com.rdc.androidinterview.util.ApiSuccessResponse
import com.rdc.androidinterview.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository
@Inject
constructor(
    val parrotChallengeApiMenuService: ParrotChallengeApiMenuService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
): JobManager("AccountRepository") {

    private val TAG: String = "AppDebug"

    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>> {
        return object: NetworkBoundResource<MyStoreResponse, AccountProperties, AccountViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            // if network is down, view the cache and return
            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main){
                    // finishing by viewing db cache
                    result.addSource(loadFromCache()){ viewState ->
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<MyStoreResponse>) {
                updateLocalDb(response.body.result)

                createCacheRequestAndReturn()
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountPropertiesDao.searchByPk(authToken.account_pk!!)
                    .switchMap {
                        object: LiveData<AccountViewState>(){
                            override fun onActive() {
                                super.onActive()
                                value = AccountViewState(it)
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: AccountProperties?) {
                cacheObject?.let {
                    accountPropertiesDao.updateAccountProperties(
                        pk = 1,
                        username = cacheObject.username,
                        email = cacheObject.email.orEmpty(),
                        uuid = cacheObject.uuid.orEmpty(),
                        stores = cacheObject.stores.orEmpty()
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<MyStoreResponse>> {
                return parrotChallengeApiMenuService
                    .getAccountProperties(
                        "Bearer ${authToken.access_token!!}"
                    )
            }


            override fun setJob(job: Job) {
                addJob("getAccountProperties", job)
            }

        }.asLiveData()
    }

}












