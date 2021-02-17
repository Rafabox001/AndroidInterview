package com.rdc.androidinterview.ui.menu.menu

import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.MenuItem
import com.rdc.androidinterview.repository.menu.account.AccountRepository
import com.rdc.androidinterview.repository.menu.menu.MenuRepository
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.BaseViewModel
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.menu.account.state.AccountStateEvent
import com.rdc.androidinterview.ui.menu.menu.state.MenuStateEvent
import com.rdc.androidinterview.ui.menu.menu.state.MenuStateEvent.*
import com.rdc.androidinterview.ui.menu.menu.state.MenuViewState
import com.rdc.androidinterview.util.AbsentLiveData
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    val sessionManager: SessionManager,
    val menuRepository: MenuRepository,
    val requestManager: RequestManager
) : BaseViewModel<MenuStateEvent, MenuViewState>() {

    override fun handleStateEvent(stateEvent: MenuStateEvent): LiveData<DataState<MenuViewState>> {
        when (stateEvent) {
            is SearchMenuItemsEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    menuRepository.searchMenuItems(authToken, stateEvent.storeId)
                } ?: AbsentLiveData.create()
            }
            is None -> {
                return AbsentLiveData.create()
            }
            is GetAccountPropertiesEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    menuRepository.getAccountProperties(authToken)
                } ?: AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): MenuViewState {
        return MenuViewState()
    }

    fun setMenuListData(menuList: List<MenuItem>) {
        val update = getCurrentViewStateOrNew()
        update.menuList = menuList
        _viewState.value = update
    }

    fun setAccountPropertiesData(accountProperties: AccountProperties){
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties){
            return
        }
        update.accountProperties = accountProperties
        _viewState.value = update
    }

    fun cancelActiveJobs() {
        menuRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData() {
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}