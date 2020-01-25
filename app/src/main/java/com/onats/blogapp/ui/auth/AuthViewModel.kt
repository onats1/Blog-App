package com.onats.blogapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onats.blogapp.api.auth.networkResponses.LoginResponse
import com.onats.blogapp.api.auth.networkResponses.RegistrationResponse
import com.onats.blogapp.models.AuthToken
import com.onats.blogapp.repository.auth.AuthRepository
import com.onats.blogapp.ui.BaseViewModel
import com.onats.blogapp.ui.DataState
import com.onats.blogapp.ui.auth.states.AuthStateEvent
import com.onats.blogapp.ui.auth.states.AuthStateEvent.*
import com.onats.blogapp.ui.auth.states.AuthViewState
import com.onats.blogapp.ui.auth.states.LoginFields
import com.onats.blogapp.ui.auth.states.RegistrationFields
import com.onats.blogapp.util.AbsentLiveData
import com.onats.blogapp.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): BaseViewModel<AuthStateEvent, AuthViewState>()
{

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when (stateEvent){
            is LoginAttemptEvent -> {
                return AbsentLiveData.create()
            }

            is RegisterAttemptEvent -> {
                return AbsentLiveData.create()
            }

            is CheckPreviousAuthEvent -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentviewStateOrNew()
        if (update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentviewStateOrNew()
        if (update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentviewStateOrNew()
        if (update.authToken == authToken){
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }

}