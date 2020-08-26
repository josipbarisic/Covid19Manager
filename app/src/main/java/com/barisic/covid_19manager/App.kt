package com.barisic.covid_19manager

import android.app.Application
import com.barisic.covid_19manager.modules.networkModule
import com.barisic.covid_19manager.modules.repositoryModule
import com.barisic.covid_19manager.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

//        registerReceiver(TestingReceiver(), IntentFilter(Intent.ACTION_SCREEN_OFF))

        startKoin {
            androidContext(this@App)
            loadKoinModules(
                listOf(
                    viewModelModule,
                    networkModule,
                    repositoryModule
                )
            )
        }
    }
}