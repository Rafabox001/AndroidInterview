package com.rdc.androidinterview.di.menu

import com.rdc.androidinterview.api.menu.ParrotChallengeApiMenuService
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.MenuItemDao
import com.rdc.androidinterview.persistence.ParrotChallengeDatabase
import com.rdc.androidinterview.repository.menu.account.AccountRepository
import com.rdc.androidinterview.repository.menu.menu.MenuRepository
import com.rdc.androidinterview.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MenuModule {

    @MenuScope
    @Provides
    fun provideParrotChallengeApiMenuService(retrofitBuilder: Retrofit.Builder): ParrotChallengeApiMenuService {
        return retrofitBuilder
            .build()
            .create(ParrotChallengeApiMenuService::class.java)
    }

    @MenuScope
    @Provides
    fun provideAccountRepository(
        parrotChallengeApiMenuService: ParrotChallengeApiMenuService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepository(
            parrotChallengeApiMenuService,
            accountPropertiesDao,
            sessionManager
        )
    }

    @MenuScope
    @Provides
    fun provideMenuRepository(
        parrotChallengeApiMenuService: ParrotChallengeApiMenuService,
        menuItemDao: MenuItemDao,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): MenuRepository {
        return MenuRepository(
            parrotChallengeApiMenuService,
            menuItemDao,
            accountPropertiesDao,
            sessionManager
        )
    }

    @MenuScope
    @Provides
    fun provideMenuItemDao(db: ParrotChallengeDatabase): MenuItemDao {
        return db.getMenuItemDao()
    }

}