package com.onats.blogapp.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.onats.blogapp.api.auth.BlogApiAuthService
import com.onats.blogapp.api.auth.networkResponses.LoginResponse
import com.onats.blogapp.models.AuthToken
import com.onats.blogapp.persistence.AccountPropertiesDao
import com.onats.blogapp.persistence.AuthTokenDao
import com.onats.blogapp.repository.NetworkBoundResource
import com.onats.blogapp.session.SessionManager
import com.onats.blogapp.ui.DataState
import com.onats.blogapp.ui.Response
import com.onats.blogapp.ui.ResponseType
import com.onats.blogapp.ui.auth.states.AuthViewState
import com.onats.blogapp.ui.auth.states.LoginFields
import com.onats.blogapp.util.ApiEmptyResponse
import com.onats.blogapp.util.ApiErrorResponse
import com.onats.blogapp.util.ApiSuccessResponse
import com.onats.blogapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.onats.blogapp.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.onats.blogapp.util.GenericApiResponse
import kotlinx.coroutines.Job
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

        val loginError = LoginFields(email, password).isValidForLogin()

        if (loginError != LoginFields.LoginError.none()) {
            returnErrorResponse(loginError, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ) {

            override suspend fun handleApiSuccesResponse(response: ApiSuccessResponse<LoginResponse>) {
                if (response.body.response == GENERIC_AUTH_ERROR){
                    return onErrorReturn(response.body.errorMessage, shouldUsedialog = true, shouldUseToast = false)
                }

                onCompleteJob(DataState.data(
                    data = AuthViewState(
                        authToken = AuthToken(response.body.pk, response.body.token)
                    )
                ))
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return blogApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }.asLiveData()
    }

    private fun returnErrorResponse(
        errorMessage: String,
        responseType: ResponseType
    ): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    response = Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    /*  fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
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
      }*/

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