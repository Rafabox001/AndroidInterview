package com.rdc.androidinterview.ui.menu.account.state

sealed class AccountStateEvent{

    class GetAccountPropertiesEvent: AccountStateEvent()

    class None: AccountStateEvent()

}