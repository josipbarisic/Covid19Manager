package com.barisic.covid_19manager.models

data class LokacijaPacijenta(
    val korisnikId: Long,
    val lat: Float,
    val long: Float,
    val vrijeme: Long
)