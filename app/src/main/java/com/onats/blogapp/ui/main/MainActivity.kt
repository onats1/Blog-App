package com.onats.blogapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.onats.blogapp.R
import com.onats.blogapp.ui.BaseActivity
import com.onats.blogapp.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeObservers()

        tool_bar.setOnClickListener {

            Log.d("main", "toolbar clicked.")
            sessionManager.logout()
        }

    }

    private fun subscribeObservers(){

        sessionManager.cachedToken.observe(this, Observer { authToken ->

            if(authToken == null || authToken.account_pk == -1 || authToken.token == null){
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)

        startActivity(intent)
        finish()
    }
}