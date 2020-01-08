package com.onats.blogapp.ui.auth


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.onats.blogapp.R
import com.onats.blogapp.util.ApiEmptyResponse
import com.onats.blogapp.util.ApiErrorResponse
import com.onats.blogapp.util.ApiSuccessResponse

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

        viewModel.testLogin().observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is ApiSuccessResponse -> {
                    Log.d( TAG, "Login response: ${response.body}")
                }
                is ApiErrorResponse -> {
                    Log.d( TAG, "Login response: ${response.errorMessage}")
                }
                is ApiEmptyResponse -> {
                    Log.d( TAG, "Login response: $response")
                }
            }
        })
    }


}
