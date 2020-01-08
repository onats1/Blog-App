package com.onats.blogapp.api.auth

import androidx.lifecycle.LiveData
import com.onats.blogapp.api.auth.networkResponses.LoginResponse
import com.onats.blogapp.api.auth.networkResponses.RegistrationResponse
import com.onats.blogapp.util.GenericApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BlogApiAuthService{

    @POST("account/login")
    @FormUrlEncoded
    fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LiveData<GenericApiResponse<LoginResponse>>

    @POST("account/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password2") password2: String
    ): LiveData<GenericApiResponse<RegistrationResponse>>
}