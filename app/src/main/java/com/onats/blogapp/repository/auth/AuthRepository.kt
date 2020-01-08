package com.onats.blogapp.repository.auth

import androidx.lifecycle.LiveData
import com.onats.blogapp.api.auth.BlogApiAuthService
import com.onats.blogapp.api.auth.networkResponses.LoginResponse
import com.onats.blogapp.api.auth.networkResponses.RegistrationResponse
import com.onats.blogapp.persistence.AccountPropertiesDao
import com.onats.blogapp.persistence.AuthTokenDao
import com.onats.blogapp.session.SessionManager
import com.onats.blogapp.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val blogApiAuthService: BlogApiAuthService,
    val sessionManager: SessionManager
) {

    fun testLoginRequest(email: String, password: String): LiveData<GenericApiResponse<LoginResponse>>{
        return blogApiAuthService.login(email, password)
    }

    fun testRegistrationRequest(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<GenericApiResponse<RegistrationResponse>>{
        return blogApiAuthService.register(
            email = email,
            username = username,
            password = password,
            password2 = confirmPassword
        )
    }


}