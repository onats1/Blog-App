package com.onats.blogapp.di.auth

import com.onats.blogapp.api.auth.BlogApiAuthService
import com.onats.blogapp.persistence.AccountPropertiesDao
import com.onats.blogapp.persistence.AuthTokenDao
import com.onats.blogapp.repository.auth.AuthRepository
import com.onats.blogapp.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule{

    @AuthScope
    @Provides
    fun provideApiService(retrofitBuilder: Retrofit.Builder): BlogApiAuthService{
        return retrofitBuilder
            .build()
            .create(BlogApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        blogApiAuthService: BlogApiAuthService
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            blogApiAuthService,
            sessionManager
        )
    }

}