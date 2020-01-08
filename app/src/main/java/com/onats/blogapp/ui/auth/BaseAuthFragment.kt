package com.onats.blogapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onats.blogapp.di.ViewModelProviderFactory
import com.onats.blogapp.ui.viewModels.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAuthFragment: DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        }?: throw Exception("Invalid Activity")
    }
}