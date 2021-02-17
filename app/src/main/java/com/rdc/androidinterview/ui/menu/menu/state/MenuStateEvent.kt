package com.rdc.androidinterview.ui.menu.menu.state

sealed class MenuStateEvent{

    class GetAccountPropertiesEvent: MenuStateEvent()

    class SearchMenuItemsEvent(val storeId: String): MenuStateEvent()

    class None: MenuStateEvent()

}