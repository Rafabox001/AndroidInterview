package com.rdc.androidinterview.repository.menu.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.rdc.androidinterview.api.menu.ParrotChallengeApiMenuService
import com.rdc.androidinterview.api.menu.network_requests.MenuUpdateRequest
import com.rdc.androidinterview.api.menu.network_responses.MenuListResponse
import com.rdc.androidinterview.api.menu.network_responses.MenuUpdateResponse
import com.rdc.androidinterview.api.menu.network_responses.MyStoreResponse
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.AuthToken
import com.rdc.androidinterview.models.MenuItem
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.MenuItemDao
import com.rdc.androidinterview.repository.JobManager
import com.rdc.androidinterview.repository.NetworkBoundResource
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.auth.state.AuthViewState
import com.rdc.androidinterview.ui.menu.menu.state.MenuViewState
import com.rdc.androidinterview.util.AbsentLiveData
import com.rdc.androidinterview.util.ApiSuccessResponse
import com.rdc.androidinterview.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuRepository @Inject constructor(
    val parrotChallengeApiMenuService: ParrotChallengeApiMenuService,
    val menuItemDao: MenuItemDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
): JobManager("MenuRepository"){

    private val TAG: String = "AppDebug"

    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<MenuViewState>> {
        return object: NetworkBoundResource<MyStoreResponse, AccountProperties, MenuViewState>(
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

            override fun loadFromCache(): LiveData<MenuViewState> {
                return accountPropertiesDao.searchByPk(authToken.account_pk!!)
                    .switchMap {
                        object: LiveData<MenuViewState>(){
                            override fun onActive() {
                                super.onActive()
                                value = MenuViewState(it)
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

    fun searchMenuItems(
        authToken: AuthToken,
        storeId: String
    ): LiveData<DataState<MenuViewState>> {
        return object: NetworkBoundResource<MenuListResponse, List<MenuItem>, MenuViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main){
                    // finishing by viewing db cache
                    result.addSource(loadFromCache()){ viewState ->
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<MenuListResponse>
            ) {

                val menuItemList: ArrayList<MenuItem> = ArrayList()
                for(menuItemResponse in response.body.results){
                    menuItemList.add(
                        MenuItem(
                            uuid = menuItemResponse.uuid,
                            name = menuItemResponse.name,
                            description = menuItemResponse.description,
                            imageUrl = menuItemResponse.imageUrl,
                            legacyId = menuItemResponse.legacyId,
                            price = menuItemResponse.price,
                            alcoholCount = menuItemResponse.alcoholCount,
                            soldAlone = menuItemResponse.soldAlone,
                            availability = menuItemResponse.availability,
                            category = menuItemResponse.category
                        )
                    )
                }
                updateLocalDb(menuItemList)

                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<MenuListResponse>> {
                return parrotChallengeApiMenuService.searchListMenuItems(
                    "Bearer ${authToken.access_token!!}",
                    storeId = storeId
                )
            }

            override fun loadFromCache(): LiveData<MenuViewState> {
                return menuItemDao.getAllMenuItems()
                    .switchMap {
                        object: LiveData<MenuViewState>(){
                            override fun onActive() {
                                super.onActive()
                                value = MenuViewState(
                                    menuList = it
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<MenuItem>?) {
                // loop through list and update the local db
                if(cacheObject != null){
                    withContext(Dispatchers.IO) {
                        for(menuItem in cacheObject){
                            try{
                                // Launch each insert as a separate job to be executed in parallel
                                val j = launch {
                                    Log.d(TAG, "updateLocalDb: inserting menuItem: ${menuItem}")
                                    menuItemDao.insert(menuItem)
                                }
                                j.join() // wait for completion before proceeding to next
                            }catch (e: Exception){
                                Log.e(TAG, "updateLocalDb: error updating cache data on menu item with uuid: ${menuItem.uuid}. " +
                                        "${e.message}")
                                // Could send an error report here or something but I don't think you should throw an error to the UI
                                // Since there could be many blog posts being inserted/updated.
                            }
                        }
                    }
                }
                else{
                    Log.d(TAG, "updateLocalDb: menu items list is null")
                }
            }

            override fun setJob(job: Job) {
                addJob("searchMenuItems", job)
            }

        }.asLiveData()
    }

    fun updateMenuItem(
        authToken: AuthToken,
        productId: String,
        availability: String
    ): LiveData<DataState<MenuViewState>> {
        return object: NetworkBoundResource<MenuUpdateResponse, MenuItem, MenuViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            false
        ) {

            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main){
                    // finishing by viewing db cache
                    result.addSource(loadFromCache()){ viewState ->
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<MenuUpdateResponse>
            ) {
                updateLocalDb(response.body.result)

                onCompleteJob(
                    DataState.data(
                        data = MenuViewState(
                            updatedMenuItem = response.body.result
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<MenuUpdateResponse>> {
                return parrotChallengeApiMenuService.updateMenuItem(
                    "Bearer ${authToken.access_token!!}",
                    product_id = productId,
                    MenuUpdateRequest(availability = availability)
                )
            }

            //Not used in this case
            override fun loadFromCache(): LiveData<MenuViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: MenuItem?) {
                cacheObject?.let {
                    menuItemDao.updateMenuItem(cacheObject.uuid, cacheObject.availability)
                }
            }

            override fun setJob(job: Job) {
                addJob("searchMenuItems", job)
            }

        }.asLiveData()
    }
}