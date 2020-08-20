package com.barisic.covid_19manager.modules

import com.barisic.covid_19manager.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single { PacijentRepository(get()) }
    single { EpidemiologRepository(get()) }
    single { StanjePacijentaRepository(get()) }
    single { LokacijaPacijentaRepository(get(), get()) }
    single { JsonWebTokenRepository(get()) }
}