package com.barisic.covid_19manager.modules

import com.barisic.covid_19manager.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(androidContext(), get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { StanjePacijentaViewModel(androidContext(), get()) }
    viewModel { InfoViewModel() }
    viewModel { PovijestStanjaViewModel(androidContext(), get()) }
    viewModel { PorukaViewModel(androidContext(), get()) }
}