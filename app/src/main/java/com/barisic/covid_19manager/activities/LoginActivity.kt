package com.barisic.covid_19manager.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.ActivityLoginBinding
import com.barisic.covid_19manager.util.RESULT_SUCCESS
import com.barisic.covid_19manager.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel.lifecycleOwner = this
        viewModel.result.observe(this, Observer {
            when (it == RESULT_SUCCESS) {
                true -> {
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = FLAG_ACTIVITY_CLEAR_TOP

                    startActivity(i)
                    finishAffinity()
                }
            }
        })
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this
    }
}
