package com.barisic.covid_19manager.modules

import com.barisic.covid_19manager.viewmodels.InfoViewModel
import com.barisic.covid_19manager.viewmodels.LoginViewModel
import com.barisic.covid_19manager.viewmodels.PovijestStanjaViewModel
import com.barisic.covid_19manager.viewmodels.StanjePacijentaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { StanjePacijentaViewModel(get(), get(), get()) }
    viewModel { InfoViewModel() }
    viewModel { PovijestStanjaViewModel() }
}