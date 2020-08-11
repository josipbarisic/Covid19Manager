package com.barisic.covid_19manager.modules

import com.barisic.covid_19manager.interfaces.JSONWebTokenInterface
import com.barisic.covid_19manager.interfaces.LokacijaPacijentaInterface
import com.barisic.covid_19manager.interfaces.PacijentInterface
import com.barisic.covid_19manager.interfaces.StanjePacijentaInterface
import com.barisic.covid_19manager.util.BASE_API_URL
import com.barisic.covid_19manager.util.JSON_TOKEN_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val builder = OkHttpClient.Builder()
        builder.callTimeout(10, TimeUnit.SECONDS)
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    }

    single(qualifier = named("BaseUrl")) {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_API_URL)
            .build()
    }

    single(qualifier = named("JsonTokenUrl")) {
        Retrofit.Builder()
            .client(get())
            .baseUrl(JSON_TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(named("BaseUrl")).create(PacijentInterface::class.java)
    }

    single {
        get<Retrofit>(named("BaseUrl")).create(StanjePacijentaInterface::class.java)
    }

    single {
        get<Retrofit>(named("BaseUrl")).create(LokacijaPacijentaInterface::class.java)
    }

    single {
        get<Retrofit>(named("JsonTokenUrl")).create(JSONWebTokenInterface::class.java)
    }
}