package com.rdc.androidinterview.ui.menu.account

import androidx.lifecycle.LiveData
import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.repository.menu.account.AccountRepository
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.BaseViewModel
import com.rdc.androidinterview.ui.DataState
import com.rdc.androidinterview.ui.auth.state.AuthStateEvent
import com.rdc.androidinterview.ui.menu.account.state.AccountStateEvent
import com.rdc.androidinterview.ui.menu.account.state.AccountViewState
import com.rdc.androidinterview.util.AbsentLiveData
import javax.inject.Inject

class AccountViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val accountRepository: AccountRepository
)
    : BaseViewModel<AccountStateEvent, AccountViewState>()
{
    override fun handleStateEvent(stateEvent: AccountStateEvent): LiveData<DataState<AccountViewState>> {
        when(stateEvent){

            is AccountStateEvent.GetAccountPropertiesEvent ->{
                return sessionManager.cachedToken.value?.let { authToken ->
                    accountRepository.getAccountProperties(authToken)
                }?: AbsentLiveData.create()
            }
            is AccountStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun setAccountPropertiesData(accountProperties: AccountProperties){
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties){
            return
        }
        update.accountProperties = accountProperties
        _viewState.value = update
    }

    fun logout(){
        sessionManager.logout()
    }

    private fun handlePendingData(){
        setStateEvent(AccountStateEvent.None())
    }

    fun cancelActiveJobs(){
        handlePendingData()
        accountRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}














