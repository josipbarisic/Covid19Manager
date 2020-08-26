package com.barisic.covid_19manager.modules

import com.barisic.covid_19manager.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel() }
    viewModel { StanjePacijentaViewModel(get(), get(), get()) }
    viewModel { InfoViewModel() }
    viewModel { PovijestStanjaViewModel(get(), get(), get()) }
    viewModel { PorukaViewModel(get(), get(), get()) }
}