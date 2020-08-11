package com.barisic.covid_19manager.models

data class Pacijent(
    val id: Long,
    val oib: Long,
    val ime: String,
    val prezime: String,
    val adresaSi: String,
    val lat: Float,
    val long: Float,
    var status: Int
)