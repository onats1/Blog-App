package com.onats.blogapp.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onats.blogapp.models.AuthToken
import com.onats.blogapp.persistence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authToken: AuthTokenDao,
    val application: Application
){
    private val TAG: String = "AppDebug"

    val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken
    
    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    fun logout(){
        var errorMessage: String? = null

        try {
            cachedToken.value!!.account_pk?.let{
                authToken.nullifyToken(it)
                Log.e(TAG, "$it")
            }
        }catch (e: CancellationException){
            errorMessage = e.message
            Log.e(TAG, errorMessage)
        }catch (e: Exception){
            errorMessage += "\n ${e.message}"
            Log.e(TAG, errorMessage)

        }finally {
            errorMessage?.let {
                Log.e(TAG, errorMessage)
            }
        }
        setValue(null)
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main){
            if (_cachedToken.value != newValue){
                _cachedToken.value = newValue
            }
        }
    }

    fun isConnectedToTheInternet(): Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        }catch (e: Exception){

        }
        return false
    }
}