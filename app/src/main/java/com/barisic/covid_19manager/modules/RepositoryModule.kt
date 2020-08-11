package com.barisic.covid_19manager.modules

import com.barisic.covid_19manager.repositories.JsonWebTokenRepository
import com.barisic.covid_19manager.repositories.LokacijaPacijentaRepository
import com.barisic.covid_19manager.repositories.PacijentRepository
import com.barisic.covid_19manager.repositories.StanjePacijentaRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { PacijentRepository(get()) }
    single { StanjePacijentaRepository(get()) }
    single { LokacijaPacijentaRepository(get(), get()) }
    single { JsonWebTokenRepository(get()) }
}