package com.onats.blogapp.di

import com.onats.blogapp.di.auth.AuthFragmentBuildersModule
import com.onats.blogapp.di.auth.AuthModule
import com.onats.blogapp.di.auth.AuthScope
import com.onats.blogapp.di.auth.AuthViewModelModule
import com.onats.blogapp.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}