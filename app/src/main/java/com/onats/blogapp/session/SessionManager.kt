package com.onats.blogapp.session

import android.app.Application
import com.onats.blogapp.persistence.AuthTokenDao
import javax.inject.Inject

class SessionManager
@Inject
constructor(
    val authToken: AuthTokenDao,
    val application: Application
){

}