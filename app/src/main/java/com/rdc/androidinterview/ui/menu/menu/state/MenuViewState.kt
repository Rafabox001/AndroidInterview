package com.rdc.androidinterview.ui.menu.menu.state

import com.rdc.androidinterview.models.AccountProperties
import com.rdc.androidinterview.models.MenuItem

class MenuViewState(
    var accountProperties: AccountProperties? = null,
    var menuList: List<MenuItem> = listOf(),
    var updatedMenuItem: MenuItem? = null
)