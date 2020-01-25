package com.onats.blogapp.ui.auth


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.onats.blogapp.R
import com.onats.blogapp.models.AuthToken
import com.onats.blogapp.ui.auth.states.AuthStateEvent
import com.onats.blogapp.ui.auth.states.LoginFields
import com.onats.blogapp.util.ApiEmptyResponse
import com.onats.blogapp.util.ApiErrorResponse
import com.onats.blogapp.util.ApiSuccessResponse
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseAuthFragment() {

    val TAG = "login fragment."

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        login_button.setOnClickListener {
            login()
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let { loginFields ->
                loginFields.login_email?.let { input_email.setText(it)}
                loginFields.login_password?.let { input_password.setText(it)}
            }
        })
    }

    private fun login(){
        viewModel.setStateEvent(
            AuthStateEvent.LoginAttemptEvent(
                email = input_email.text.toString(),
                password = input_password.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.setLoginFields(
            LoginFields(
                login_email = input_email.text.toString(),
                login_password = input_password.text.toString()
            )
        )
    }


}
