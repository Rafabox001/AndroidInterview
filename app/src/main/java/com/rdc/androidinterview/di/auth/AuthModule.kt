package com.rdc.androidinterview.di.auth

import com.rdc.androidinterview.api.auth.ParrotChallengeApiAuthService
import com.rdc.androidinterview.persistence.AccountPropertiesDao
import com.rdc.androidinterview.persistence.AuthTokenDao
import com.rdc.androidinterview.repository.auth.AuthRepository
import com.rdc.androidinterview.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule{

    // TEMPORARY
    @AuthScope
    @Provides
    fun provideFakeApiService(): ParrotChallengeApiAuthService{
        return Retrofit.Builder()
            .baseUrl("http://api-staging.parrot.rest")
            .build()
            .create(ParrotChallengeApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        parrotChallengeApiAuthService: ParrotChallengeApiAuthService
        ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            parrotChallengeApiAuthService,
            sessionManager
        )
    }

}