package com.onats.blogapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onats.blogapp.api.auth.networkResponses.LoginResponse
import com.onats.blogapp.api.auth.networkResponses.RegistrationResponse
import com.onats.blogapp.repository.auth.AuthRepository
import com.onats.blogapp.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): ViewModel()
{

    fun testLogin(): LiveData<GenericApiResponse<LoginResponse>>{
        return authRepository.testLoginRequest(
            "third@user.com",
            "Onatsdayo777?"
        )
    }

    fun testRegistration(): LiveData<GenericApiResponse<RegistrationResponse>>{
        return authRepository.testRegistrationRequest(
            "fourth@user.com",
            "fourthuser",
            "Onatsdayo777?",
            "Onatsdayo777?"
        )
    }
}