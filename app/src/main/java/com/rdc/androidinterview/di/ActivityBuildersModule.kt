package com.rdc.androidinterview.di

import com.rdc.androidinterview.di.auth.AuthFragmentBuildersModule
import com.rdc.androidinterview.di.auth.AuthModule
import com.rdc.androidinterview.di.auth.AuthScope
import com.rdc.androidinterview.di.auth.AuthViewModelModule
import com.rdc.androidinterview.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}