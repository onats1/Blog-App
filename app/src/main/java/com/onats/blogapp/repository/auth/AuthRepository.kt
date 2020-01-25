package com.onats.blogapp.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.onats.blogapp.api.auth.BlogApiAuthService
import com.onats.blogapp.models.AuthToken
import com.onats.blogapp.persistence.AccountPropertiesDao
import com.onats.blogapp.persistence.AuthTokenDao
import com.onats.blogapp.session.SessionManager
import com.onats.blogapp.ui.DataState
import com.onats.blogapp.ui.Response
import com.onats.blogapp.ui.ResponseType
import com.onats.blogapp.ui.auth.states.AuthViewState
import com.onats.blogapp.util.ApiEmptyResponse
import com.onats.blogapp.util.ApiErrorResponse
import com.onats.blogapp.util.ApiSuccessResponse
import com.onats.blogapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val blogApiAuthService: BlogApiAuthService,
    val sessionManager: SessionManager
) {

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        return blogApiAuthService.login(email, password)
            .switchMap { genericApiResponse ->

                object : LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()

                        when (genericApiResponse) {

                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    data = AuthViewState(
                                        authToken = AuthToken(
                                            token = genericApiResponse.body.token,
                                            account_pk = genericApiResponse.body.pk
                                        )
                                    ),
                                    response = null
                                )
                            }


                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = genericApiResponse.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }

                        }

                    }
                }
            }
    }

    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        return blogApiAuthService.register(username, email, password, confirmPassword)
            .switchMap { genericApiResponse ->

                object : LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()

                        when (genericApiResponse) {

                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    data = AuthViewState(
                                        authToken = AuthToken(
                                            token = genericApiResponse.body.token,
                                            account_pk = genericApiResponse.body.pk
                                        )
                                    ),
                                    response = null
                                )
                            }


                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = genericApiResponse.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }

                        }

                    }
                }
            }
    }

}