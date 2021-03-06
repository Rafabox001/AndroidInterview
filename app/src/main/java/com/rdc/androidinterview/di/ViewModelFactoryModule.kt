package com.rdc.androidinterview.di

import androidx.lifecycle.ViewModelProvider
import com.rdc.androidinterview.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}