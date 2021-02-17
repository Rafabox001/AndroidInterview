package com.rdc.androidinterview.di.menu

import com.rdc.androidinterview.ui.menu.account.AccountFragment
import com.rdc.androidinterview.ui.menu.menu.MenuFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MenuFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeMenuFragment(): MenuFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

}