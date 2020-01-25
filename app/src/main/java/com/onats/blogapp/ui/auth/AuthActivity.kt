package com.onats.blogapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onats.blogapp.R
import com.onats.blogapp.di.ViewModelProviderFactory
import com.onats.blogapp.ui.BaseActivity
import com.onats.blogapp.ui.ResponseType
import com.onats.blogapp.ui.main.MainActivity
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)

        subscribeObservers()
    }

    private fun subscribeObservers(){

        viewModel.dataState.observe(this, Observer { dataState ->

            dataState.data?.let {  data ->
                data.data?.let { event ->

                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            viewModel.setAuthToken(it)
                        }
                    }

                }

                data.response?.let {event ->

                    event.getContentIfNotHandled()?.let {response ->

                        when (response.responseType) {
                            is ResponseType.Dialog -> {

                            }

                            is ResponseType.Toast -> {

                            }

                            is ResponseType.None -> {

                            }

                        }

                    }

                }
            }



        })

        viewModel.viewState.observe(this, Observer {
            it.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { authToken ->
            if(authToken != null && authToken.account_pk != -1 && authToken.token != null){
                navMainActivity()
            }
        })
    }

    private fun navMainActivity() {

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
        finish()
    }


}
