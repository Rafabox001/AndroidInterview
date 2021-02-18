package com.rdc.androidinterview.di.menu

import androidx.lifecycle.ViewModel
import com.rdc.androidinterview.di.ViewModelKey
import com.rdc.androidinterview.ui.menu.account.AccountViewModel
import com.rdc.androidinterview.ui.menu.menu.MenuViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MenuViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    abstract fun bindMenuViewModel(menuViewModel: MenuViewModel): ViewModel

}