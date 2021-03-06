package com.onats.blogapp.di.auth

import androidx.lifecycle.ViewModel
import com.onats.blogapp.di.ViewModelKey
import com.onats.blogapp.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}